package com.relaxed.pool.monitor.monitor;

import cn.hutool.core.thread.ThreadUtil;
import com.relaxed.pool.monitor.AlertService;
import com.relaxed.pool.monitor.ThreadPoolMonitorProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 高级线程池监控器，提供动态监控、预警和自动调整线程池参数的功能。 该类负责监控注册的线程池，收集性能指标，并在必要时触发告警或自动调整线程池配置。
 *
 * @author Yakir
 * @since 1.0
 */
@Slf4j
@Setter
@Getter
public class ThreadPoolTaskMonitor {

	// 被监控的线程池映射表
	private final Map<String, MonitoredThreadPool> threadPoolMap = new ConcurrentHashMap<>();

	// 历史数据，用于趋势分析
	private final Map<String, List<ThreadPoolStats>> historyStats = new ConcurrentHashMap<>();

	private final ThreadPoolMonitorProperties monitorProperties;

	// 告警服务
	private final AlertService alertService;

	public ThreadPoolTaskMonitor(AlertService alertService, ThreadPoolMonitorProperties monitorProperties) {
		this.alertService = alertService;
		this.monitorProperties = monitorProperties;
		// 判断是否开启池监控功能
		if (this.monitorProperties.isMonitorEnabled()) {
			this.startMonitorThread();
		}
	}

	/**
	 * 注册线程池到监控系统。 如果监控功能未启用，将直接返回原始线程池。
	 * @param name 线程池名称，用于标识和监控
	 * @param executor 要监控的线程池对象
	 * @param <T> 线程池类型
	 * @return 包装后的线程池对象，如果监控未启用则返回原始线程池
	 */
	public <T extends ThreadPoolExecutor> ThreadPoolExecutor register(String name, T executor) {
		if (!monitorProperties.isMonitorEnabled()) {
			return executor;
		}
		MonitoredThreadPool monitoredPool = new MonitoredThreadPool(name, executor);
		return this.register(name, monitoredPool);
	}

	/**
	 * 注册已包装的线程池到监控系统。 将线程池添加到监控映射表中，并初始化其历史统计数据。
	 * @param name 线程池名称
	 * @param monitoredPool 已包装的线程池对象
	 * @return 包装后的线程池对象
	 */
	public ThreadPoolExecutor register(String name, MonitoredThreadPool monitoredPool) {
		ThreadPoolExecutor executor = monitoredPool.getOriginalExecutor();
		threadPoolMap.put(name, monitoredPool);
		historyStats.put(name, new ArrayList<>());
		log.info("线程池 [{}] 已注册到监控系统, 核心线程数: {}, 最大线程数: {}, 队列类型: {}, 队列容量: {}", name, executor.getCorePoolSize(),
				executor.getMaximumPoolSize(), executor.getQueue().getClass().getSimpleName(),
				getQueueCapacity(executor.getQueue()));

		return monitoredPool;
	}

	/**
	 * 启动监控线程，定期收集线程池统计数据。 监控线程会按照配置的时间间隔运行，直到程序终止。
	 */
	private void startMonitorThread() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						collectStats();
					}
					catch (Exception e) {

					}
					ThreadUtil.sleep(monitorProperties.getMonitorIntervalMills());
				}
			}
		});
		thread.setName("pool-monitor-daemon");
		thread.setDaemon(true);
		thread.start();

	}

	/**
	 * 收集所有注册线程池的统计数据。 包括线程池大小、队列大小、活跃线程数等指标。
	 */
	public void collectStats() {
		if (!monitorProperties.isMonitorEnabled() || threadPoolMap.isEmpty()) {
			return;
		}

		threadPoolMap.forEach((name, pool) -> {
			ThreadPoolStats stats = new ThreadPoolStats();
			stats.setTimestamp(System.currentTimeMillis());
			stats.setPoolName(name);
			stats.setActiveThreads(pool.getActiveCount());
			stats.setCorePoolSize(pool.getCorePoolSize());
			stats.setMaximumPoolSize(pool.getMaximumPoolSize());
			stats.setLargestPoolSize(pool.getLargestPoolSize());
			stats.setPoolSize(pool.getPoolSize());
			stats.setQueueSize(pool.getQueue().size());
			stats.setQueueCapacity(getQueueCapacity(pool.getQueue()));
			stats.setTaskCount(pool.getTaskCount());
			stats.setCompletedTaskCount(pool.getCompletedTaskCount());
			stats.setRejectedCount(pool.getRejectedCount());

			// 计算指标
			calculateMetrics(stats);

			// 存储历史数据
			List<ThreadPoolStats> history = historyStats.get(name);
			history.add(stats);

			// 只保留最近50条记录
			if (history.size() > 50) {
				history.remove(0);
			}

			// 检查是否需要告警
			checkAlert(stats);

			log.debug("线程池 [{}] 状态: 活跃线程 {}/{} ({}%), 队列 {}/{} ({}%), 已完成任务 {}, 拒绝任务 {}", name,
					stats.getActiveThreads(), stats.getMaximumPoolSize(), stats.getActiveThreadRatio(),
					stats.getQueueSize(), stats.getQueueCapacity(), stats.getQueueUsageRatio(),
					stats.getCompletedTaskCount(), stats.getRejectedCount());
			// 检查是否需要自动调整线程池参数
			if (stats.getActiveThreadRatio() > 90 && stats.getQueueUsageRatio() > 70) {
				autoAdjustThreadPool(pool);
			}
			// 恢复初始线程参数

			if (isRestoreThreadPool(pool, stats)) {
				restoreThreadPool(pool);
			}
		});
	}

	/**
	 * 判断线程池是否需要恢复到原始配置。 当线程池处于自动调整状态且满足恢复条件时返回true。
	 * @param pool 被监控的线程池
	 * @param stats 当前线程池统计信息
	 * @return 如果需要恢复则返回true
	 */
	private boolean isRestoreThreadPool(MonitoredThreadPool pool, ThreadPoolStats stats) {
		long idleRatioMaxThreshold = monitorProperties.getIdleRatioMaxThreshold();
		long idleRatioIntervalMills = monitorProperties.getIdleRatioIntervalMills();
		int originalMaximumPoolSize = pool.getOriginalExecutor().getMaximumPoolSize();
		int maximumPoolSize = pool.getMaximumPoolSize();

		return maximumPoolSize > originalMaximumPoolSize && stats.getActiveThreadRatio() < idleRatioMaxThreshold
				&& stats.getQueueUsageRatio() < idleRatioMaxThreshold
				&& System.currentTimeMillis() - pool.getLastUpdateTimeMills() > idleRatioIntervalMills;
	}

	/**
	 * 将线程池恢复到原始配置。 重置核心线程数和最大线程数到初始值。
	 * @param executor 要恢复的线程池
	 */
	private void restoreThreadPool(MonitoredThreadPool executor) {
		String poolName = getPoolName(executor);
		int originalMaximumPoolSize = executor.getOriginalExecutor().getMaximumPoolSize();

		int maximumPoolSize = executor.getMaximumPoolSize();
		log.info("自动调整线程池 [{}] 恢复原线程数: {} -> {}", poolName, maximumPoolSize, originalMaximumPoolSize);
		executor.setMaximumPoolSize(originalMaximumPoolSize);
		executor.setLastUpdateTimeMills(System.currentTimeMillis());

	}

	/**
	 * 获取指定线程池的趋势数据。 返回最近一段时间内的性能指标变化趋势。
	 * @param poolName 线程池名称
	 * @return 线程池趋势数据对象
	 */
	public ThreadPoolTrend getTrend(String poolName) {
		List<ThreadPoolStats> history = historyStats.getOrDefault(poolName, Collections.emptyList());
		if (history.isEmpty()) {
			return null;
		}

		ThreadPoolTrend trend = new ThreadPoolTrend();
		trend.setPoolName(poolName);
		// 计算平均最大线程数
		trend.setAvgMaximumPoolSize(
				history.stream().mapToDouble(ThreadPoolStats::getMaximumPoolSize).average().orElse(0));
		// 计算平均活跃线程比例趋势
		trend.setAvgActiveThreadRatio(
				history.stream().mapToDouble(ThreadPoolStats::getActiveThreadRatio).average().orElse(0));

		// 计算平均队列使用率趋势
		trend.setAvgQueueUsageRatio(
				history.stream().mapToDouble(ThreadPoolStats::getQueueUsageRatio).average().orElse(0));

		// 计算拒绝任务数趋势
		trend.setTotalRejectedCount(history.stream().mapToLong(ThreadPoolStats::getRejectedCount).sum());

		// 计算任务完成率趋势
		trend.setTaskCompletionRatio(
				history.stream().mapToDouble(ThreadPoolStats::getTaskCompletionRatio).average().orElse(100));

		return trend;
	}

	/**
	 * 获取所有线程池的当前统计信息。
	 * @return 所有线程池统计信息的列表
	 */
	public List<ThreadPoolStats> getAllPoolStats() {
		return threadPoolMap.keySet().stream().map(name -> {
			List<ThreadPoolStats> history = historyStats.getOrDefault(name, Collections.emptyList());
			return history.isEmpty() ? null : history.get(history.size() - 1);
		}).filter(Objects::nonNull).collect(Collectors.toList());
	}

	/**
	 * 计算线程池的性能指标。 包括队列使用率、线程活跃度等关键指标。
	 * @param stats 要计算的统计信息对象
	 */
	private void calculateMetrics(ThreadPoolStats stats) {
		// 活跃线程比例
		stats.setActiveThreadRatio(stats.getMaximumPoolSize() > 0
				? (double) stats.getActiveThreads() / stats.getMaximumPoolSize() * 100 : 0);

		// 队列使用率
		stats.setQueueUsageRatio(
				stats.getQueueCapacity() > 0 ? (double) stats.getQueueSize() / stats.getQueueCapacity() * 100 : 0);

		// 任务完成率
		stats.setTaskCompletionRatio(
				stats.getTaskCount() > 0 ? (double) stats.getCompletedTaskCount() / stats.getTaskCount() * 100 : 100);
	}

	/**
	 * 检查线程池状态并触发告警。 当线程池指标超过阈值时，通过告警服务发送通知。
	 * @param stats 线程池统计信息
	 */
	private void checkAlert(ThreadPoolStats stats) {
		boolean needAlert = false;
		StringBuilder alertMsg = new StringBuilder();
		Integer alertThreshold = monitorProperties.getAlertThreshold();
		// 检查活跃线程比例
		if (stats.getActiveThreadRatio() > alertThreshold) {
			needAlert = true;
			alertMsg.append("活跃线程比例过高: ").append(String.format("%.1f%%", stats.getActiveThreadRatio())).append("; ");
		}

		// 检查队列使用率

		if (stats.getQueueUsageRatio() > alertThreshold) {
			needAlert = true;
			alertMsg.append("队列使用率过高: ").append(String.format("%.1f%%", stats.getQueueUsageRatio())).append("; ");
		}

		// 检查拒绝任务数
		if (stats.getRejectedCount() > 0) {
			needAlert = true;
			alertMsg.append("存在任务被拒绝: ").append(stats.getRejectedCount()).append("个; ");
		}

		if (needAlert) {
			String finalMsg = String.format("线程池告警 [%s]: %s", stats.getPoolName(), alertMsg);
			alertService.sendAlert(finalMsg, monitorProperties.getAlertChannels().split(","));
		}
	}

	/**
	 * 自动调整线程池参数。 根据当前负载情况动态调整核心线程数和最大线程数。
	 * @param executor 要调整的线程池
	 */
	private void autoAdjustThreadPool(MonitoredThreadPool executor) {
		// 未开启自动调整线程池参数 直接返回
		if (!monitorProperties.isAdjustPoolNumEnabled()) {
			return;
		}
		// 控制最大线程数 不能无限扩大
		int adjustPoolMaxinumThreshold = monitorProperties.getAdjustPoolMaxinumThreshold();
		int currentMax = executor.getMaximumPoolSize();
		// 当前最大线程数 大过上限阈值 则不在自动扩增
		if (currentMax >= adjustPoolMaxinumThreshold) {
			return;
		}
		int newMax = Math.min(currentMax + 5, currentMax * 2); // 最多翻倍

		if (newMax > adjustPoolMaxinumThreshold) {
			newMax = adjustPoolMaxinumThreshold;
		}
		log.info("自动调整线程池 [{}] 最大线程数: {} -> {}", getPoolName(executor), currentMax, newMax);

		executor.setMaximumPoolSize(newMax);
		// 设置最后一次更新时间
		executor.setLastUpdateTimeMills(System.currentTimeMillis());
	}

	/**
	 * 获取线程池的名称。 如果线程池未命名，则使用其哈希码作为名称。
	 * @param executor 线程池对象
	 * @return 线程池名称
	 */
	private String getPoolName(ThreadPoolExecutor executor) {
		for (Map.Entry<String, MonitoredThreadPool> entry : threadPoolMap.entrySet()) {
			// if (entry.getValue().getOriginalExecutor() == executor) {
			// 当前传进来的线程池已经为包装后的线程池 此处不用获取原线程池
			if (entry.getValue() == executor) {
				return entry.getKey();
			}
		}
		return "unknown";
	}

	/**
	 * 获取阻塞队列的容量。 支持ArrayBlockingQueue和LinkedBlockingQueue类型的队列。
	 * @param queue 要检查的阻塞队列
	 * @return 队列容量，如果无法获取则返回-1
	 */
	private int getQueueCapacity(BlockingQueue<?> queue) {
		try {
			if (queue instanceof LinkedBlockingQueue) {
				Field field = LinkedBlockingQueue.class.getDeclaredField("capacity");
				field.setAccessible(true);
				return (int) field.get(queue);
			}
			else if (queue instanceof ArrayBlockingQueue) {
				Field field = ArrayBlockingQueue.class.getDeclaredField("items");
				field.setAccessible(true);
				Object[] items = (Object[]) field.get(queue);
				return items.length;
			}
		}
		catch (Exception e) {
			log.warn("获取队列容量失败", e);
		}

		return Integer.MAX_VALUE; // 默认为无界队列
	}

}

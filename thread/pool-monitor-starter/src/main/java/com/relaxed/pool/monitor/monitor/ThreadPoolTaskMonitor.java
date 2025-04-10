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
 * @author Yakir
 * @Topic ThreadPoolMonitor
 * @Description 高级线程池监控器 支持动态监控、预警和自动调整线程池参数
 * @date 2025/4/3 16:40
 * @Version 1.0
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
	 * 注册线程池到监控系统
	 * @param name 线程池名称
	 * @param executor 线程池对象
	 * @return 包装后的线程池对象
	 */
	public <T extends ThreadPoolExecutor> ThreadPoolExecutor register(String name, T executor) {
		if (!monitorProperties.isMonitorEnabled()) {
			return executor;
		}
		MonitoredThreadPool monitoredPool = new MonitoredThreadPool(name, executor);
		return this.register(name, monitoredPool);
	}

	/**
	 * 注册包装后的
	 * @param name
	 * @param monitoredPool
	 * @return
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
	 * 定时收集线程池统计数据
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
	 * 是否恢复初始状态 判断条件 当前最大池数大于原始数 且空闲率小于最小阈值 ,距离上次更新时间超过空闲率间隔
	 * @param pool
	 * @param stats
	 * @return true重置 false不重置
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

	private void restoreThreadPool(MonitoredThreadPool executor) {
		String poolName = getPoolName(executor);
		int originalMaximumPoolSize = executor.getOriginalExecutor().getMaximumPoolSize();

		int maximumPoolSize = executor.getMaximumPoolSize();
		log.info("自动调整线程池 [{}] 恢复原线程数: {} -> {}", poolName, maximumPoolSize, originalMaximumPoolSize);
		executor.setMaximumPoolSize(originalMaximumPoolSize);
		executor.setLastUpdateTimeMills(System.currentTimeMillis());

	}

	/**
	 * 获取线程池使用趋势分析
	 * @param poolName 线程池名称
	 * @return 趋势数据
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
	 * 获取所有线程池状态快照
	 * @return 线程池状态列表
	 */
	public List<ThreadPoolStats> getAllPoolStats() {
		return threadPoolMap.keySet().stream().map(name -> {
			List<ThreadPoolStats> history = historyStats.getOrDefault(name, Collections.emptyList());
			return history.isEmpty() ? null : history.get(history.size() - 1);
		}).filter(Objects::nonNull).collect(Collectors.toList());
	}

	/**
	 * 计算线程池指标
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
	 * 检查是否需要发送告警
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
	 * 自动调整线程池参数
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
	 * 获取线程池名称
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
	 * 获取队列容量
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

package com.relaxed.pool.monitor.monitor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 被监控的线程池类，继承自 ThreadPoolExecutor。 用于包装原始的线程池执行器，添加监控和统计功能。 主要功能包括： 1. 记录任务拒绝次数 2.
 * 记录线程池参数更新时间 3. 包装原始的拒绝策略
 *
 * @author Yakir
 * @since 1.0.0
 */
@Slf4j
public class MonitoredThreadPool extends ThreadPoolExecutor {

	/**
	 * 线程池名称，用于标识和监控
	 */
	private final String name;

	/**
	 * 原始的线程池执行器
	 */
	private final ThreadPoolExecutor originalExecutor;

	/**
	 * 被拒绝的任务总数
	 */
	private long rejectedCount = 0;

	/**
	 * 最后一次参数更新时间，用于记录线程池配置变更的时间点
	 */
	private long lastUpdateTimeMills;

	/**
	 * 构造一个新的被监控的线程池。 继承原始线程池的所有配置，并添加监控功能。
	 * @param name 线程池名称
	 * @param executor 原始线程池执行器
	 */
	public MonitoredThreadPool(String name, ThreadPoolExecutor executor) {
		super(executor.getCorePoolSize(), executor.getMaximumPoolSize(),
				executor.getKeepAliveTime(TimeUnit.NANOSECONDS), TimeUnit.NANOSECONDS, executor.getQueue(),
				executor.getThreadFactory(), executor.getRejectedExecutionHandler());

		this.name = name;
		this.originalExecutor = executor;
		this.lastUpdateTimeMills = System.currentTimeMillis();
		// 包装拒绝策略
		this.setRejectedExecutionHandler(new RejectedHandler(executor.getRejectedExecutionHandler()));
	}

	/**
	 * 获取原始的线程池执行器
	 * @return 原始线程池执行器
	 */
	public ThreadPoolExecutor getOriginalExecutor() {
		return originalExecutor;
	}

	/**
	 * 获取被拒绝的任务总数
	 * @return 被拒绝的任务总数
	 */
	public long getRejectedCount() {
		return rejectedCount;
	}

	/**
	 * 获取最后一次参数更新时间
	 * @return 最后一次参数更新时间
	 */
	public long getLastUpdateTimeMills() {
		return lastUpdateTimeMills;
	}

	/**
	 * 设置最后一次参数更新时间
	 * @param lastUpdateTimeMills 最后一次参数更新时间
	 */
	public void setLastUpdateTimeMills(long lastUpdateTimeMills) {
		this.lastUpdateTimeMills = lastUpdateTimeMills;
	}

	/**
	 * 获取线程池名称
	 * @return 线程池名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 拒绝策略包装器，用于记录被拒绝的任务数量。 在原始拒绝策略执行前，增加拒绝计数。
	 */
	private class RejectedHandler implements RejectedExecutionHandler {

		/**
		 * 原始的拒绝策略处理器
		 */
		private final RejectedExecutionHandler original;

		/**
		 * 构造一个新的拒绝策略包装器
		 * @param original 原始的拒绝策略处理器
		 */
		public RejectedHandler(RejectedExecutionHandler original) {
			this.original = original;
		}

		/**
		 * 处理被拒绝的任务。 增加拒绝计数，记录日志，然后调用原始的拒绝策略。
		 * @param r 被拒绝的任务
		 * @param executor 线程池执行器
		 */
		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			rejectedCount++;
			log.warn("线程池 [{}] 拒绝任务，当前拒绝总数: {}", name, rejectedCount);
			original.rejectedExecution(r, executor);
		}

	}

}

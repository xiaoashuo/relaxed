package com.relaxed.pool.monitor.monitor;

import lombok.Data;

/**
 * 线程池统计信息类，用于存储线程池的各项监控指标。 包含线程池的基本信息、运行状态和性能指标等统计数据。
 *
 * @author Yakir
 * @since 1.0.0
 */
@Data
public class ThreadPoolStats {

	/**
	 * 统计信息的时间戳
	 */
	private long timestamp;

	/**
	 * 线程池名称
	 */
	private String poolName;

	/**
	 * 当前活跃线程数
	 */
	private int activeThreads;

	/**
	 * 核心线程数
	 */
	private int corePoolSize;

	/**
	 * 最大线程数
	 */
	private int maximumPoolSize;

	/**
	 * 线程池曾经达到的最大线程数
	 */
	private int largestPoolSize;

	/**
	 * 当前线程池大小
	 */
	private int poolSize;

	/**
	 * 当前队列中等待执行的任务数
	 */
	private int queueSize;

	/**
	 * 任务队列的容量
	 */
	private int queueCapacity;

	/**
	 * 线程池接收到的任务总数
	 */
	private long taskCount;

	/**
	 * 已完成的任务总数
	 */
	private long completedTaskCount;

	/**
	 * 被拒绝的任务总数
	 */
	private long rejectedCount;

	/**
	 * 活跃线程比例 计算公式：activeThreads / maximumPoolSize
	 */
	private double activeThreadRatio;

	/**
	 * 队列使用率 计算公式：queueSize / queueCapacity
	 */
	private double queueUsageRatio;

	/**
	 * 任务完成率 计算公式：completedTaskCount / taskCount
	 */
	private double taskCompletionRatio;

}

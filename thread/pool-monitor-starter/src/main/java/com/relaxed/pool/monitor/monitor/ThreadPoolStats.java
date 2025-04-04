package com.relaxed.pool.monitor.monitor;

import lombok.Data;

/**
 * @author Yakir
 * @Topic ThreadPoolStats
 * @Description 线程池监控统计信息
 * @date 2025/4/3 16:56
 * @Version 1.0
 */
@Data
public class ThreadPoolStats {

	private long timestamp;

	private String poolName;

	private int activeThreads;

	private int corePoolSize;

	private int maximumPoolSize;

	private int largestPoolSize;

	private int poolSize;

	private int queueSize;

	private int queueCapacity;

	private long taskCount;

	private long completedTaskCount;

	private long rejectedCount;

	// 计算的指标
	private double activeThreadRatio; // 活跃线程比例

	private double queueUsageRatio; // 队列使用率

	private double taskCompletionRatio; // 任务完成率

}

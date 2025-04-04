package com.relaxed.pool.monitor.monitor;

import lombok.Data;

/**
 * @author Yakir
 * @Topic ThreadPoolTrend
 * @Description 线程池趋势分析
 * @date 2025/4/4 10:28
 * @Version 1.0
 */
@Data
public class ThreadPoolTrend {

	private String poolName;

	private double avgActiveThreadRatio; // 平均活跃线程比例

	private double avgQueueUsageRatio; // 平均队列使用率

	private long totalRejectedCount; // 总拒绝任务数

	private double taskCompletionRatio; // 任务完成率

}

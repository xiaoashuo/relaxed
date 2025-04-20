package com.relaxed.pool.monitor.monitor;

import lombok.Data;

/**
 * 线程池趋势分析类，用于统计和分析线程池在一段时间内的运行趋势。 包含了线程池各项指标的平均值和累计值，用于评估线程池的整体运行状况。
 *
 * @author Yakir
 * @since 1.0.0
 */
@Data
public class ThreadPoolTrend {

	/**
	 * 线程池名称
	 */
	private String poolName;

	/**
	 * 平均最大线程数 统计周期内线程池最大线程数的平均值
	 */
	private double avgMaximumPoolSize;

	/**
	 * 平均活跃线程比例 统计周期内活跃线程数与最大线程数比例的平均值
	 */
	private double avgActiveThreadRatio;

	/**
	 * 平均队列使用率 统计周期内队列使用量与队列容量比例的平均值
	 */
	private double avgQueueUsageRatio;

	/**
	 * 总拒绝任务数 统计周期内被线程池拒绝的任务总数
	 */
	private long totalRejectedCount;

	/**
	 * 任务完成率 统计周期内已完成任务数与总任务数的比例
	 */
	private double taskCompletionRatio;

}

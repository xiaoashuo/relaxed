package com.relaxed.pool.monitor;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 线程池监控配置属性类，用于配置线程池监控的各项参数。 配置前缀为 "relaxed.thread-pool.monitor"。
 *
 * @author Yakir
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "relaxed.thread-pool.monitor")
@Data
public class ThreadPoolMonitorProperties {

	/**
	 * 是否启用线程池监控功能。 默认为 true。
	 */
	private boolean monitorEnabled = true;

	/**
	 * 线程池告警阈值，以百分比表示。 当线程池使用率超过此阈值时，将触发告警。 默认为 80%。
	 */
	private Integer alertThreshold = 80;

	/**
	 * 告警通知渠道配置。 可配置多个渠道，用于发送告警信息。
	 */
	private String alertChannels;

	/**
	 * 监控检查间隔时间，单位为毫秒。 默认为 10000 毫秒（10秒）。
	 */
	private long monitorIntervalMills = 10000;

	/**
	 * 是否启用线程池自动调整功能。 当启用时，如果最大线程数不够用，将自动扩充； 当闲置率过高且持续超过指定时间，将自动恢复到初始最大线程数。 默认为 true。
	 */
	private boolean adjustPoolNumEnabled = true;

	/**
	 * 线程池空闲率的最大阈值，以百分比表示。 当所有线程的空闲率低于此阈值，且持续时间超过指定间隔时， 将自动将线程池最大值恢复为原始设置。 默认为 20%。
	 */
	private long idleRatioMaxThreshold = 20;

	/**
	 * 空闲率检查间隔时间，单位为毫秒。 当空闲率持续低于阈值，且超过此时间间隔时， 将自动将扩充的最大线程数恢复为原始值。 默认为 60000 毫秒（1分钟）。
	 */
	private long idleRatioIntervalMills = 60000;

	/**
	 * 线程池自动调整的最大阈值。 当调整后的线程数超过此阈值时，将不再进行调整。 默认为 30。
	 */
	private int adjustPoolMaxinumThreshold = 30;

}

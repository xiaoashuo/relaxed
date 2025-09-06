package com.relaxed.common.core.util.batch.core;

import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

/**
 * BatchMetrics 性能指标
 *
 * @author Yakir
 */
@Data
public class BatchMetrics {

	/**
	 * 处理数
	 */
	private final AtomicLong processedCount = new AtomicLong(0);

	/**
	 * 成功数
	 */
	private final AtomicLong successCount = new AtomicLong(0);

	/**
	 * 失败数
	 */
	private final AtomicLong failureCount = new AtomicLong(0);

	/**
	 * 总耗时 ms
	 */
	private final AtomicLong totalTime = new AtomicLong(0);

}

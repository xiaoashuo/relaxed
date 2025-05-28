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

	private final AtomicLong processedCount = new AtomicLong(0);

	private final AtomicLong successCount = new AtomicLong(0);

	private final AtomicLong failureCount = new AtomicLong(0);

	private final AtomicLong totalTime = new AtomicLong(0);

}

package com.relaxed.common.core.util.batch.core;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * BatchExceptionStats
 *
 * @author Yakir
 */
@Data
public class BatchExceptionStats {

	private final AtomicLong totalCount = new AtomicLong(0);

	private final AtomicLong uniqueCount = new AtomicLong(0);

	private final ConcurrentHashMap<String, AtomicLong> exceptionTypeCount = new ConcurrentHashMap<>();

	private final int maxExceptions;

	/**
	 * 异常样本集合
	 */
	private final Set<Throwable> sampleExceptions;

	private final AtomicBoolean shouldStop = new AtomicBoolean(false);

	public BatchExceptionStats(int maxExceptions) {
		this.maxExceptions = maxExceptions;
		this.sampleExceptions = Collections.synchronizedSet(new LinkedHashSet<>(maxExceptions));

	}

	public void addException(Throwable e) {
		totalCount.incrementAndGet();

		// 统计异常类型
		if (e instanceof CompletionException) {
			e = e.getCause();
		}

		String exceptionType = e.getClass().getName();
		exceptionTypeCount.computeIfAbsent(exceptionType, k -> new AtomicLong(0)).incrementAndGet();

		// 采样异常
		synchronized (sampleExceptions) {
			if (sampleExceptions.size() < maxExceptions) {
				if (sampleExceptions.add(e)) { // 如果成功添加（即类型不重复）
					uniqueCount.incrementAndGet();
				}
			}
		}
	}

	public void markShouldStop() {
		shouldStop.set(true);
	}

	public boolean shouldStop() {
		return shouldStop.get();
	}

	public String getSummary() {
		StringBuilder summary = new StringBuilder();
		summary.append("异常统计:\n");
		summary.append("总异常数: ").append(totalCount.get()).append("\n");
		summary.append("采样异常数: ").append(uniqueCount.get()).append("\n");
		summary.append("异常类型分布:\n");
		exceptionTypeCount.forEach(
				(type, count) -> summary.append("  ").append(type).append(": ").append(count.get()).append("\n"));
		summary.append("异常样本类型:\n");
		sampleExceptions.forEach(exception -> summary.append("  ").append(exception.getClass().getName()).append("-")
				.append(exception.getMessage()).append("\n"));
		return summary.toString();
	}

}

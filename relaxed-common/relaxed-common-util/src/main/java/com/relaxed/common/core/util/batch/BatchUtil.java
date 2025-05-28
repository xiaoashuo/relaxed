package com.relaxed.common.core.util.batch;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.relaxed.common.core.util.batch.core.BatchConfig;
import com.relaxed.common.core.util.batch.core.BatchContext;
import com.relaxed.common.core.util.batch.core.BatchExceptionStats;
import com.relaxed.common.core.util.batch.core.BatchMeta;
import com.relaxed.common.core.util.batch.core.BatchMetrics;
import com.relaxed.common.core.util.batch.core.BatchProgress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

/**
 * BatchKit
 *
 * @author Yakir
 */
@Slf4j
public class BatchUtil {

	/**
	 * 批处理执行
	 * @param config 批处理配置
	 * @param <T> 批处理实体对象类型
	 */
	public static <T> void execute(BatchConfig<T> config) {
		// 1. 验证配置
		validateConfig(config);
		// 2. 初始化指标
		BatchMetrics metrics = new BatchMetrics();
		BatchExceptionStats exceptionStats = new BatchExceptionStats(100);
		long startTime = System.currentTimeMillis();
		// 3. 计算分组数
		int groupNum = computeGroupNum(config.getTotalCount(), config.getBatchSize());

		try {
			for (int groupNo = 1; groupNo <= groupNum && !exceptionStats.shouldStop(); groupNo++) {
				int startIndex = config.getLocationComputer() == null ? (groupNo - 1) * config.getBatchSize()
						: config.getLocationComputer().compute(groupNo, config.getBatchSize());
				int size = Math.min(config.getBatchSize(), config.getTotalCount() - startIndex);
				// 4.1 准备批次元数据
				BatchMeta batchMeta = new BatchMeta().setGroupNo(groupNo).setStartIndex(startIndex).setSize(size);
				// 4.2 获取批次数据
				List<T> dataList = config.getProvider().apply(batchMeta);
				if (CollectionUtil.isEmpty(dataList)) {
					continue;
				}
				// 4.3 处理批次数据
				if (config.isAsync()) {
					processBatchAsync(config, batchMeta, dataList, exceptionStats, metrics);
				}
				else {
					processBatchSync(config, batchMeta, dataList, exceptionStats, metrics);
				}
				// 检查是否需要停止
				if (exceptionStats.shouldStop()) {
					log.warn("检测到异常，停止处理. 当前处理到第 {} 组，共 {} 组", groupNo, groupNum);
					break;
				}
			}

		}
		finally {
			// 5. 更新总耗时
			metrics.getTotalTime().set(System.currentTimeMillis() - startTime);
		}
		// 6. 处理异常
		if (exceptionStats.getTotalCount().get() > 0 && config.getExceptionHandler() != null) {
			List<Throwable> exceptions = new ArrayList<>(exceptionStats.getSampleExceptions());
			config.getExceptionHandler().accept(exceptions);
			log.error(exceptionStats.getSummary());
		}
		// 7. 输出最终统计
		printFinalStatistics(config.getTaskName(), metrics, config.getTotalCount());

	}

	/**
	 * 异步处理批次数据
	 */
	private static <T> void processBatchAsync(BatchConfig<T> config, BatchMeta batchMeta, List<T> dataList,
			BatchExceptionStats exceptionStats, BatchMetrics metrics) {
		List<CompletableFuture<Void>> futures = new ArrayList<>();

		for (int i = 0; i < dataList.size() && !exceptionStats.shouldStop(); i++) {
			final int rowIndex = i + 1;
			final T data = dataList.get(i);

			CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
				try {
					BatchContext<T> context = BatchContext.<T>builder().batchMeta(batchMeta).rowIndex(rowIndex)
							.data(data).build();
					config.getConsumer().accept(context);
					metrics.getSuccessCount().incrementAndGet();
				}
				catch (Throwable e) {
					metrics.getFailureCount().incrementAndGet();
					// 不在这里记录异常，而是在外层统一处理
					throw e;
				}
				finally {
					updateProgress(config, metrics, batchMeta.getGroupNo());
				}
			}, config.getExecutor());

			futures.add(future);
		}

		// 等待当前批次所有任务完成

		try {
			CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).exceptionally(throwable -> {
				if (throwable != null) {
					exceptionStats.addException(throwable);
					if (config.isFailFast()) {
						exceptionStats.markShouldStop();
					}
				}
				return null;
			}).join();
		}
		catch (Exception e) {
			exceptionStats.addException(e);
			if (config.isFailFast()) {
				exceptionStats.markShouldStop();
			}
			log.error("未捕获到的异常", e);
		}

	}

	/**
	 * 同步处理批次数据
	 */
	private static <T> void processBatchSync(BatchConfig<T> config, BatchMeta batchMeta, List<T> dataList,
			BatchExceptionStats exceptionStats, BatchMetrics metrics) {
		for (int i = 0; i < dataList.size() && !exceptionStats.shouldStop(); i++) {
			final int rowIndex = i + 1;
			final T data = dataList.get(i);

			try {
				BatchContext<T> context = BatchContext.<T>builder().batchMeta(batchMeta).rowIndex(rowIndex).data(data)
						.build();
				config.getConsumer().accept(context);
				metrics.getSuccessCount().incrementAndGet();
			}
			catch (Throwable e) {
				metrics.getFailureCount().incrementAndGet();
				exceptionStats.addException(e);
				if (config.isFailFast()) {
					exceptionStats.markShouldStop();
				}
			}
			finally {
				updateProgress(config, metrics, batchMeta.getGroupNo());
			}
		}
	}

	/**
	 * 更新进度信息
	 */
	private static <T> void updateProgress(BatchConfig<T> config, BatchMetrics metrics, int groupNo) {
		int processed = (int) metrics.getProcessedCount().incrementAndGet();
		if (config.getProgressListener() != null) {
			config.getProgressListener()
					.accept(BatchProgress.builder().total(config.getTotalCount()).current(processed).groupNo(groupNo)
							.groupTotal(computeGroupNum(config.getTotalCount(), config.getBatchSize())).build());
		}
	}

	/**
	 * 计算分组数目
	 * @param totalCount 总数
	 * @param size 批次大小
	 * @return 分组数
	 */
	private static int computeGroupNum(int totalCount, int size) {
		int groupNum = totalCount % size != 0 ? (totalCount / size) + 1 : (totalCount / size);
		return groupNum;
	}

	/**
	 * 输出最终统计信息
	 */
	private static void printFinalStatistics(String taskName, BatchMetrics metrics, int totalCount) {
		long totalTime = metrics.getTotalTime().get();
		long processed = metrics.getProcessedCount().get();
		long success = metrics.getSuccessCount().get();
		long failure = metrics.getFailureCount().get();
		double averageSpeed = (double) processed / totalTime * 1000;

		log.info("处理完成. 任务名称:{}, 总耗时: {}ms, 总处理: {}, 成功: {}, 失败: {},  平均速度: {}/s", taskName, totalTime, processed,
				success, failure, String.format("%.2f", averageSpeed));
	}

	/**
	 * 参数校验
	 * @param config 批次配置
	 * @param <T> 业务对象
	 */
	private static <T> void validateConfig(BatchConfig<T> config) {
		if (config.getTotalCount() <= 0) {
			throw new IllegalArgumentException("totalCount must be > 0");
		}
		if (config.getBatchSize() <= 0) {
			throw new IllegalArgumentException("batchSize must be > 0");
		}
		if (config.getProvider() == null) {
			throw new IllegalArgumentException("provider required");
		}
		if (config.getConsumer() == null) {
			throw new IllegalArgumentException("consumer required");
		}
		if (config.isAsync() && config.getExecutor() == null) {
			throw new IllegalArgumentException("executor required for async");
		}
	}

	@AllArgsConstructor
	@Data
	public static class UserData {

		private String username;

		private String age;

	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		AtomicLong lastProgressTime = new AtomicLong(startTime);
		AtomicLong lastProcessedCount = new AtomicLong(0);
		AtomicLong successCount = new AtomicLong(0);
		AtomicLong failureCount = new AtomicLong(0);
		List<UserData> userDataList = new ArrayList<>();

		for (int i = 0; i < 5000; i++) {
			userDataList.add(new UserData("za" + i, "1"));
		}

		BatchConfig<UserData> config = BatchConfig.<UserData>builder().totalCount(userDataList.size()).batchSize(20)
				.async(false).executor(null).taskName("CSV用户数据处理").provider(batchMeta -> {
					Integer startIndex = batchMeta.getStartIndex();
					return ListUtil.sub(userDataList, startIndex, startIndex + batchMeta.getSize());
				}).consumer(context -> {
					UserData data = context.getData();
					System.out.println(StrUtil.format("当前数据:{}", JSONUtil.toJsonStr(data)));
					ThreadUtil.sleep(1000);
					Integer groupNo = context.getBatchMeta().getGroupNo();
					if (groupNo == 2) {
						throw new RuntimeException("moc异常");
					}

				}).exceptionHandler(exceptions -> {
					log.error("处理过程中发生异常: {}", exceptions.size());
					exceptions.forEach(e -> log.error("异常详情: ", e));
				}).progressListener(progress -> {
					if (progress.getCurrent() % 20 == 0) {
						// 计算处理速度
						long currentTime = System.currentTimeMillis();
						long elapsedTime = currentTime - startTime;
						// 计算总体处理速度
						double overallRecordsPerSecond = (double) progress.getCurrent() / elapsedTime * 1000;

						// 计算最近一段时间的处理速度
						long recentElapsedTime = currentTime - lastProgressTime.get();
						long recentProcessedCount = progress.getCurrent() - lastProcessedCount.get();
						double recentRecordsPerSecond = (double) recentProcessedCount / recentElapsedTime * 1000;

						// 更新上次进度时间和处理数量
						lastProgressTime.set(currentTime);
						lastProcessedCount.set(progress.getCurrent());

						// 预估剩余时间
						long remainingRecords = progress.getTotal() - progress.getCurrent();
						long estimatedRemainingTime = (long) (remainingRecords / overallRecordsPerSecond);

						System.out.println(StrUtil.format(
								"处理进度: {}/{} ({}%), 成功: {}, 失败: {}, " + "总体速度: {}/s, 最近速度: {}/s, " + "预计剩余时间: {}",
								progress.getCurrent(), progress.getTotal(),
								String.format("%.2f", (double) progress.getCurrent() / progress.getTotal() * 100),
								successCount.get(), failureCount.get(), String.format("%.2f", overallRecordsPerSecond),
								String.format("%.2f", recentRecordsPerSecond), formatDuration(estimatedRemainingTime)));

					}
				}).build();
		BatchUtil.execute(config);
	}

	// ... existing code ...

	private static String formatDuration(long milliseconds) {
		if (milliseconds < 0) {
			return "未知";
		}

		long seconds = milliseconds / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		long days = hours / 24;

		StringBuilder sb = new StringBuilder();

		if (days > 0) {
			sb.append(days).append("天");
		}
		if (hours % 24 > 0) {
			sb.append(hours % 24).append("小时");
		}
		if (minutes % 60 > 0) {
			sb.append(minutes % 60).append("分钟");
		}
		if (seconds % 60 > 0 || sb.length() == 0) {
			sb.append(seconds % 60).append("秒");
		}

		return sb.toString();
	}

}

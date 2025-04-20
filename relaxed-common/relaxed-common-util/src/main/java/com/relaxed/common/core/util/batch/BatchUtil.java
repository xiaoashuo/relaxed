package com.relaxed.common.core.util.batch;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.relaxed.common.core.util.batch.core.BatchGroup;
import com.relaxed.common.core.util.batch.core.BatchMeta;
import com.relaxed.common.core.util.batch.core.BatchProps;
import com.relaxed.common.core.util.batch.core.BatchTask;
import com.relaxed.common.core.util.batch.funcs.DataConsumer;
import com.relaxed.common.core.util.batch.funcs.DataProvider;
import com.relaxed.common.core.util.batch.funcs.ExceptionHandler;
import com.relaxed.common.core.util.batch.funcs.LocationComputer;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;

/**
 * 批量任务处理工具类
 *
 * 提供批量数据处理的功能，支持： 1. 数据分组处理 2. 同步/异步执行 3. 自定义异常处理 4. 任务进度跟踪 5. 调试日志输出
 *
 * @author Yakir
 * @since 1.0
 */
@Slf4j
public class BatchUtil {

	/**
	 * 批量任务分组信息
	 */
	private BatchGroup batchGroup;

	/**
	 * 批量任务配置属性
	 */
	private BatchProps batchProps;

	/**
	 * 批量任务执行器
	 */
	private BatchTask batchTask;

	/**
	 * 任务执行线程池
	 */
	private Executor executor;

	/**
	 * 创建批量任务工具实例
	 * @return BatchUtil实例
	 */
	public static BatchUtil create() {
		return new BatchUtil();
	}

	/**
	 * 设置任务执行线程池
	 * @param executor 线程池执行器
	 * @return 当前BatchUtil实例
	 */
	public BatchUtil executor(Executor executor) {
		this.executor = executor;
		return this;
	}

	/**
	 * 获取任务参数配置器
	 * @return BatchGroup实例
	 */
	public BatchGroup params() {
		this.batchGroup = new BatchGroup(this);
		return this.batchGroup;
	}

	/**
	 * 获取任务执行器
	 * @param <T> 任务数据类型
	 * @return BatchTask实例
	 */
	public <T> BatchTask<T> task() {
		this.batchTask = new BatchTask(this);
		return this.batchTask;
	}

	/**
	 * 获取任务属性配置器
	 * @return BatchProps实例
	 */
	public BatchProps props() {
		this.batchProps = new BatchProps(this);
		return this.batchProps;
	}

	/**
	 * 获取当前线程池执行器
	 * @return Executor实例
	 */
	public Executor getExecutor() {
		return executor;
	}

	/**
	 * 执行批量任务 根据配置执行同步或异步批量任务处理
	 * @throws RuntimeException 当线程池执行器为空时抛出
	 */
	public void execute() {
		Executor executor = this.getExecutor();
		if (executor == null) {
			throw new RuntimeException("thread pool executor cannot be null");
		}
		// 任务基础信息
		String taskName = this.batchProps.getTaskName();
		boolean async = this.batchProps.isAsync();
		// 任务信息
		DataProvider provider = this.batchTask.getProvider();
		DataConsumer consumer = this.batchTask.getConsumer();
		LocationComputer locationComputer = this.batchTask.getLocationComputer();
		ExceptionHandler expResolver = this.batchTask.getExpResolver();
		// 分组信息
		long totalCount = this.batchGroup.getTotalCount();
		long groupNum = this.batchGroup.getGroupNum();
		int size = this.batchGroup.getSize();
		boolean debugLog = this.batchProps.isDebugLog();
		if (debugLog) {
			log.info("任务名称[{}],异步[{}],总数[{}],分组[{}],单批大小[{}]", taskName, async, totalCount, groupNum, size);
		}
		for (int groupNo = 1; groupNo <= groupNum; groupNo++) {

			int startIndex = locationComputer.compute(groupNo, size);
			BatchMeta batchMeta = getBatchMeta(groupNo, startIndex, size);
			if (debugLog) {
				log.info("组:[{}],起始坐标:[{}],任务开始", groupNo, startIndex, size);
			}
			List dataList = provider.get(batchMeta);
			if (CollectionUtil.isEmpty(dataList)) {
				continue;
			}
			if (async) {
				// 单批数量jobs
				List<CompletableFuture> jobs = new ArrayList<>();
				int rowIndex = 1;
				for (Object data : dataList) {
					int finalRowIndex = rowIndex;
					CompletableFuture<Void> job = CompletableFuture
							.runAsync(() -> process(data, consumer, batchMeta, finalRowIndex, expResolver), executor);
					jobs.add(job);
					rowIndex++;
				}
				// 异步任务时候 同步等待执行完成
				if (CollectionUtil.isNotEmpty(jobs)) {
					// completableFutures.forEach(CompletableFuture::join);
					try {
						CompletableFuture.allOf(jobs.toArray(new CompletableFuture[0])).join();
					}
					catch (Exception e) {
						// 捕获到第一个异常立即抛出
						throw e;
					}

				}

			}
			else {
				// 同步
				int rowIndex = 1;
				for (Object data : dataList) {
					process(data, consumer, batchMeta, rowIndex, expResolver);
					rowIndex++;
				}
			}
			if (debugLog) {
				log.info("组[{}],任务结束", groupNo, startIndex);
			}
		}
	}

	/**
	 * 处理单条数据
	 * @param data 待处理数据
	 * @param consumer 数据消费者
	 * @param batchMeta 批次元数据
	 * @param rowIndex 数据行索引
	 * @param expResolver 异常处理器
	 */
	private static void process(Object data, DataConsumer consumer, BatchMeta batchMeta, int rowIndex,
			ExceptionHandler expResolver) {
		try {
			consumer.accept(batchMeta, rowIndex, data);
		}
		catch (Exception e) {
			HashMap<String, Object> contentMap = MapUtil.of("rowIndex", rowIndex);
			contentMap.put("data", JSONUtil.toJsonStr(data));
			Throwable exp;
			if (e instanceof CompletionException) {
				exp = e.getCause();
			}
			else {
				exp = e;
			}
			expResolver.handle(batchMeta, contentMap, exp);
		}
	}

	/**
	 * 获取批次元数据
	 * @param groupNo 组号
	 * @param startIndex 起始索引
	 * @param size 批次大小
	 * @return BatchMeta实例
	 */
	private static BatchMeta getBatchMeta(int groupNo, int startIndex, int size) {
		BatchMeta batchMeta = new BatchMeta();
		batchMeta.setGroupNo(groupNo);
		batchMeta.setStartIndex(startIndex);
		batchMeta.setSize(size);
		return batchMeta;
	}

}

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
 * @author Yakir
 * @Topic BatchTask
 * @Description
 * @date 2025/1/2 14:00
 * @Version 1.0
 */
@Slf4j
public class BatchExecutor {

	private BatchGroup batchGroup;

	private BatchProps batchProps;

	private BatchTask batchTask;

	/**
	 * 线程池
	 */
	private Executor executor;

	public static BatchExecutor create() {
		return new BatchExecutor();
	}

	public BatchExecutor executor(Executor executor) {
		this.executor = executor;
		return this;
	}

	public BatchGroup params() {
		this.batchGroup = new BatchGroup(this);
		return this.batchGroup;
	}

	public <T> BatchTask<T> task() {
		this.batchTask = new BatchTask(this);
		return this.batchTask;
	}

	public BatchProps props() {
		this.batchProps = new BatchProps(this);
		return this.batchProps;
	}

	public Executor getExecutor() {
		return executor;
	}

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
		log.info("任务名称[{}],异步[{}],总数[{}],分组[{}],单批大小[{}]", taskName, async, totalCount, groupNum, size);

		for (int groupNo = 1; groupNo <= groupNum; groupNo++) {

			int startIndex = locationComputer.compute(groupNo, size);
			BatchMeta batchMeta = getBatchMeta(groupNo, startIndex, size);
			log.info("组:[{}],起始坐标:[{}],任务开始", groupNo, startIndex, size);
			List dataList = provider.get(batchMeta);
			if (CollectionUtil.isEmpty(dataList)) {
				break;
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
			log.info("组[{}],任务结束", groupNo, startIndex);
		}
	}

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

	private static BatchMeta getBatchMeta(int groupNo, int startIndex, int size) {
		BatchMeta batchMeta = new BatchMeta();
		batchMeta.setGroupNo(groupNo);
		batchMeta.setStartIndex(startIndex);
		batchMeta.setSize(size);
		return batchMeta;
	}

}

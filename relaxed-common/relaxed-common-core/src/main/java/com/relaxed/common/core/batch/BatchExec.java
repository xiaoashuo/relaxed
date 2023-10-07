package com.relaxed.common.core.batch;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.core.util.StrUtil;
import com.relaxed.common.core.batch.base.BatchMeta;
import com.relaxed.common.core.batch.base.DataConsumer;
import com.relaxed.common.core.batch.base.GroupMeta;
import com.relaxed.common.core.batch.base.LocationComputer;
import com.relaxed.common.core.batch.base.Step;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Yakir
 * @Topic BatchExec
 * @Description
 * @date 2023/10/7 10:00
 * @Version 1.0
 */
@Slf4j
public class BatchExec {

	/**
	 * 线程
	 */
	private Executor executor;

	/**
	 * 执行步骤
	 */
	private Step step;

	public BatchExec() {

	}

	public BatchExec(Step step) {
		this.step = step;
	}

	public BatchExec(Executor executor, Step step) {
		this.executor = executor;
		this.step = step;
	}

	public void setStep(Step step) {
		this.step = step;
	}

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

	public void exec() {
		String taskName = step.getTaskName();
		boolean async = step.isAsync();
		GroupMeta groupMeta = step.getGroupMeta();
		log.info("任务名称[{}],异步[{}],总数[{}],分组[{}],单批大小[{}]", taskName, async, groupMeta.getTotal(),
				groupMeta.getGroupNum(), groupMeta.getSize());
		LocationComputer locationComputer = step.getLocationComputer();
		long groupNum = groupMeta.getGroupNum();
		int size = groupMeta.getSize();
		for (int groupNo = 1; groupNo <= groupNum; groupNo++) {
			int startIndex = locationComputer.compute(groupNo, size);
			log.info("批次编号[{}],起始坐标[{}],批次大小[{}]任务开始", groupNo, startIndex, size);
			BatchMeta batchMeta = getBatchMeta(size, groupNo, startIndex);
			// 根据当前提供批次获取 待处理分组数据
			List dataList = getDataList(step, batchMeta);
			if (dataList == null) {
				continue;
			}
			if (async) {
				Executor useExecutor = this.getExecutor();
				// 异步处理任务
				List<CompletableFuture> jobs = new ArrayList<>();
				int rowIndex = 1;
				for (Object data : dataList) {
					int finalRowIndex = rowIndex;

					CompletableFuture<Void> job = CompletableFuture
							.runAsync(() -> process(step, batchMeta, finalRowIndex, data), useExecutor);
					jobs.add(job);
					rowIndex++;
				}
				// 异步任务时候 同步等待执行完成
				if (CollectionUtil.isNotEmpty(jobs)) {
					// completableFutures.forEach(CompletableFuture::join);
					CompletableFuture.allOf(jobs.toArray(new CompletableFuture[0])).join();
				}

			}
			else {
				// 同步处理任务
				int rowIndex = 1;
				for (Object data : dataList) {
					process(step, batchMeta, rowIndex, data);
					rowIndex++;
				}

			}
			log.info("批次编号[{}],起始坐标[{}],任务结束", groupNo, startIndex);

		}

	}

	private static void process(Step step, BatchMeta batchMeta, int rowIndex, Object data) {
		DataConsumer consumer = step.getConsumer();
		try {
			consumer.accept(batchMeta, rowIndex, data);
		}
		catch (Exception e) {
			log.error("消费数据异常,线程名称{},分组编号{},起始坐标{},批次大小{},数据行编号{}", Thread.currentThread().getName(),
					batchMeta.getGroupNo(), batchMeta.getStartIndex(), batchMeta.getSize(), rowIndex, e);
			step.getExceptionHandler().handle(e);
		}
	}

	private static List getDataList(Step step, BatchMeta providerMeta) {
		List dataList = null;
		try {
			dataList = step.getDataByProviderMeta(providerMeta);
		}
		catch (Exception e) {
			log.error("获取数据异常,线程名称{},分组编号{},起始坐标{},批次大小{}", Thread.currentThread().getName(), providerMeta.getGroupNo(),
					providerMeta.getStartIndex(), providerMeta.getSize(), e);
			step.getExceptionHandler().handle(e);
		}
		return dataList;
	}

	private Executor getExecutor() {
		if (executor == null) {
			ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 10, 3000, TimeUnit.SECONDS,
					new ArrayBlockingQueue<>(100));
			poolExecutor.setThreadFactory(new NamedThreadFactory("relaxed:batch:", false));
			executor = poolExecutor;
		}
		return executor;
	}

	private static BatchMeta getBatchMeta(int size, int groupNo, int startIndex) {
		BatchMeta providerMeta = new BatchMeta();
		providerMeta.setGroupNo(groupNo);
		providerMeta.setStartIndex(startIndex);
		providerMeta.setSize(size);
		return providerMeta;
	}

}

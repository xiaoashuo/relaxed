package com.relaxed.common.core.batch;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.relaxed.common.core.batch.functions.BatchConsumer;
import com.relaxed.common.core.batch.functions.BatchSupplier;
import com.relaxed.common.core.batch.functions.ExceptionHandler;

import com.relaxed.common.core.batch.params.BatchGroup;

import com.relaxed.common.core.batch.params.BatchParam;
import com.relaxed.common.core.batch.params.DataWrapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Yakir
 * @Topic BatchOps
 * @Description
 * @date 2021/7/9 11:33
 * @Version 1.0
 */
@Data
@Slf4j
public abstract class AbstractBatchOps {

	/**
	 * 运行批处理
	 */
	public <T> void runBatch(BatchParam<T> batchParam) {
		Assert.notNull(batchParam, "batch param can not be null.");
		// 基础参数获取
		String taskName = batchParam.getTaskName();
		boolean async = batchParam.isAsync();
		// 组相关
		BatchGroup batchGroup = batchParam.getBatchGroup();
		int size = batchGroup.getSize();
		long groupNum = batchGroup.getGroupNum();
		// 数据提供者
		BatchSupplier<T> batchSupplier = batchParam.getBatchSupplier();
		// 数据消费者
		BatchConsumer<T> batchConsumer = batchParam.getBatchConsumer();
		ExceptionHandler<T> exceptionHandler = batchParam.getExceptionHandler();
		log.info("{}-batch process start...", taskName);
		StopWatch stopWatch = new StopWatch(taskName);
		stopWatch.start();
		List<CompletableFuture> completableFutures = new ArrayList<>();
		for (int i = 1; i <= groupNum; i++) {
			// 起始处理值
			int batchStartIndex = computeStartPos(i, size);
			if (async) {
				completableFutures.add(runAsync(taskName, batchStartIndex, batchGroup, batchSupplier, batchConsumer,
						exceptionHandler));
			}
			else {
				process(taskName, batchStartIndex, batchGroup, batchSupplier, batchConsumer, exceptionHandler);
			}
		}
		// 异步任务时候 同步等待执行完成
		if (CollectionUtil.isNotEmpty(completableFutures)) {
			completableFutures.forEach(CompletableFuture::join);
		}
		stopWatch.stop();
		log.info("{}-batch process end time {} ms...", taskName, stopWatch.getTotalTimeMillis());
	}

	/**
	 * 处理步骤模板 //TODO 后续将 生产者 消费者 提供成 reader writer 增加监听 与此处解耦
	 * @param taskName 任务名称
	 * @param batchStartIndex 起始pos
	 * @param batchGroup 批处理组
	 * @param batchSupplier 数据提供者
	 * @param batchConsumer 数据消费者
	 */
	protected <T> void process(String taskName, int batchStartIndex, BatchGroup batchGroup,
			BatchSupplier<T> batchSupplier, BatchConsumer<T> batchConsumer, ExceptionHandler<T> exceptionHandler) {
		// 获取数据
		List<T> dataList = getData(taskName, batchStartIndex, batchGroup, batchSupplier, exceptionHandler);
		// 消费数据
		consumerData(taskName, batchStartIndex, batchGroup, batchConsumer, exceptionHandler, dataList);

	}

	private <T> void consumerData(String taskName, int batchStartIndex, BatchGroup batchGroup,
			BatchConsumer<T> batchConsumer, ExceptionHandler<T> exceptionHandler, List<T> dataList) {
		if (dataList == null) {
			return;
		}
		int batchSize = batchGroup.getSize();
		for (int i = 0; i < batchSize; i++) {
			T data = dataList.get(i);
			int rangePosIndex = i + 1;
			// 构造消费者入参
			DataWrapper dataWrapper = new DataWrapper<T>().batchStartPosIndex(batchStartIndex).batchGroup(batchGroup)
					.currentPosIndex(batchStartIndex + rangePosIndex).data(data);
			try {
				// 消费消费者
				batchConsumer.consumer(dataWrapper);
			}
			catch (Exception e) {
				exceptionHandler.collect(taskName, dataWrapper, e);
			}
		}
	}

	private <T> List<T> getData(String taskName, int batchStartIndex, BatchGroup batchGroup,
			BatchSupplier<T> batchSupplier, ExceptionHandler<T> exceptionHandler) {
		int batchSize = batchGroup.getSize();
		List<T> dataList = null;
		try {
			dataList = batchSupplier.apply(batchStartIndex, batchSize);
		}
		catch (Exception e) {
			// 构造消费者入参
			DataWrapper dataWrapper = new DataWrapper<T>().batchStartPosIndex(batchStartIndex)
					.currentPosIndex(batchStartIndex).batchGroup(batchGroup);
			exceptionHandler.collect(taskName, dataWrapper, e);
		}
		return dataList;
	}

	/**
	 * 计算起始位置
	 * @param currentIndex 当前组索引
	 * @param size 批次大小
	 * @return
	 */
	protected int computeStartPos(int currentIndex, int size) {
		return (currentIndex - 1) * size;
	}

	/**
	 * 获取线程执行器
	 * @return
	 */
	protected abstract Executor executor();

	/**
	 * 无返回值异步
	 * @param taskName
	 * @param batchStartIndex
	 * @param batchGroup
	 * @return
	 */
	private <T> CompletableFuture<Void> runAsync(String taskName, Integer batchStartIndex, BatchGroup batchGroup,
			BatchSupplier<T> batchSupplier, BatchConsumer<T> batchConsumer, ExceptionHandler<T> exceptionHandler) {
		// 此处将异常自己捕获 忽略原始的异常捕获
		return CompletableFuture.runAsync(
				() -> process(taskName, batchStartIndex, batchGroup, batchSupplier, batchConsumer, exceptionHandler),
				executor());

	}

}

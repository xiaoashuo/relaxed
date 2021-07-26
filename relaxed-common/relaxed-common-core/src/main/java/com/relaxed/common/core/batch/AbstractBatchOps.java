package com.relaxed.common.core.batch;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.relaxed.common.core.batch.functions.BatchConsumer;
import com.relaxed.common.core.batch.functions.BatchSupplier;
import com.relaxed.common.core.batch.params.BatchConsumerParam;
import com.relaxed.common.core.batch.params.BatchExceptionParam;
import com.relaxed.common.core.batch.params.BatchGroup;

import com.relaxed.common.core.batch.params.BatchParam;
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
	 * 错误处理器 错误处理回调运行在调用者线程，若不做特殊处理 则程序抛出异常中断
	 *
	 */
	private Consumer<BatchExceptionParam> errorHandler = batchExceptionParam -> {
		throw new BatchException(
				StrUtil.format("当前任务名称:{} 当前位置:{} 批处理组:{}", batchExceptionParam.getTaskName(),
						batchExceptionParam.getCurrentStepPosition(), batchExceptionParam.getBatchGroup()),
				batchExceptionParam.getThrowable());
	};

	/**
	 * 运行批处理
	 */
	public void runBatch(BatchParam batchParam) {
		Assert.notNull(batchParam, "batch param can not be null.");
		// 基础参数获取
		String taskName = batchParam.getTaskName();
		boolean async = batchParam.isAsync();
		// 组相关
		BatchGroup batchGroup = batchParam.getBatchGroup();
		int size = batchGroup.getSize();
		Integer groupNum = batchGroup.getGroupNum();
		// 数据提供者
		BatchSupplier batchSupplier = batchParam.getBatchSupplier();
		// 数据消费者
		BatchConsumer batchConsumer = batchParam.getBatchConsumer();
		log.info("{}-batch process start...", taskName);
		StopWatch stopWatch = new StopWatch(taskName);
		stopWatch.start();
		List<CompletableFuture> completableFutures = new ArrayList<>(groupNum);
		for (int i = 1; i <= groupNum; i++) {
			// 起始处理值
			int currentStepPosition = computeStartPos(i, size);
			if (async) {
				completableFutures
						.add(runAsync(currentStepPosition, taskName, batchGroup, batchSupplier, batchConsumer));
			}
			else {
				try {
					process(currentStepPosition, batchGroup, batchSupplier, batchConsumer);
				}
				catch (Exception throwable) {
					BatchExceptionParam batchExceptionParam = BatchExceptionParam.of(taskName, currentStepPosition,
							batchGroup, throwable);
					errorHandler.accept(batchExceptionParam);
				}
			}
		}
		// 异步任务时候 同步等待执行完成
		if (CollectionUtil.isNotEmpty(completableFutures)) {
			completableFutures.forEach(completableFuture -> completableFuture.join());
		}
		stopWatch.stop();
		log.info("{}-batch process end time {} ms...", taskName, stopWatch.getTotalTimeMillis());
	}

	/**
	 * 处理步骤模板
	 * @param currentStepPosition 起始pos
	 * @param batchGroup 批处理组
	 * @param batchSupplier 数据提供者
	 * @param batchConsumer 数据消费者
	 */
	protected void process(int currentStepPosition, BatchGroup batchGroup, BatchSupplier batchSupplier,
			BatchConsumer batchConsumer) {
		// 获取数据
		List list = batchSupplier.apply(currentStepPosition, batchGroup.getSize());
		// 构造消费者入参
		BatchConsumerParam consumerData = buildConsumerParam(currentStepPosition, batchGroup, list);
		// 消费消费者
		batchConsumer.consumer(consumerData);
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
	 * 构建批处理消费者参数
	 * @param currentStepPosition
	 * @param batchGroup
	 * @param list
	 * @return
	 */
	protected BatchConsumerParam buildConsumerParam(Integer currentStepPosition, BatchGroup batchGroup, List list) {
		BatchConsumerParam consumerData = new BatchConsumerParam<>();
		consumerData.setBatchGroup(batchGroup);
		consumerData.setCurrentStepPosition(currentStepPosition);
		consumerData.setData(list);
		return consumerData;
	}

	/**
	 * 获取线程执行器
	 * @return
	 */
	protected abstract Executor executor();

	/**
	 * 无返回值异步
	 * @param currentStepPosition
	 * @param taskName
	 * @param batchGroup
	 * @param batchSupplier
	 * @param batchConsumer
	 * @return
	 */
	private CompletableFuture<Void> runAsync(Integer currentStepPosition, String taskName, BatchGroup batchGroup,
			BatchSupplier batchSupplier, BatchConsumer batchConsumer) {
		return runAsync(currentStepPosition, taskName, batchGroup,
				() -> process(currentStepPosition, batchGroup, batchSupplier, batchConsumer));
	}

	/**
	 * 无返回值异步
	 * @param currentStepPosition
	 * @param taskName
	 * @param batchGroup
	 * @param runnable
	 * @return
	 */
	private CompletableFuture<Void> runAsync(Integer currentStepPosition, String taskName, BatchGroup batchGroup,
			Runnable runnable) {
		return runAsync(currentStepPosition, taskName, batchGroup, runnable, errorHandler);
	}

	/**
	 * 有返回值异步
	 * @param currentStepPosition
	 * @param taskName
	 * @param batchGroup
	 * @param supplier
	 * @param <U>
	 * @return
	 */
	private <U> CompletableFuture<U> supplyAsync(Integer currentStepPosition, String taskName, BatchGroup batchGroup,
			Supplier<U> supplier) {
		return supplyAsync(currentStepPosition, taskName, batchGroup, supplier, errorHandler);
	}

	/**
	 * 无返回值异步
	 * @param currentStepPosition
	 * @param taskName
	 * @param batchGroup
	 * @param runnable
	 * @param errorHandler
	 * @return
	 */
	private CompletableFuture<Void> runAsync(Integer currentStepPosition, String taskName, BatchGroup batchGroup,
			Runnable runnable, Consumer<BatchExceptionParam> errorHandler) {
		return CompletableFuture.runAsync(runnable, executor()).exceptionally(throwable -> {
			BatchExceptionParam batchExceptionParam = BatchExceptionParam.of(taskName, currentStepPosition, batchGroup,
					throwable);
			errorHandler.accept(batchExceptionParam);
			return null;
		});
	}

	/**
	 * 有返回值异步
	 * @param currentStepPosition
	 * @param taskName
	 * @param batchGroup
	 * @param supplier
	 * @param errorHandler
	 * @param <U>
	 * @return
	 */
	private <U> CompletableFuture<U> supplyAsync(Integer currentStepPosition, String taskName, BatchGroup batchGroup,
			Supplier<U> supplier, Consumer<BatchExceptionParam> errorHandler) {
		return CompletableFuture.supplyAsync(supplier, executor()).exceptionally(throwable -> {
			BatchExceptionParam batchExceptionParam = BatchExceptionParam.of(taskName, currentStepPosition, batchGroup,
					throwable);
			errorHandler.accept(batchExceptionParam);
			return null;
		});
	}

}

package com.relaxed.common.core.batch;

import cn.hutool.core.collection.CollectionUtil;
import com.relaxed.common.core.batch.functions.BatchConsumer;
import com.relaxed.common.core.batch.functions.BatchSupplier;
import com.relaxed.common.core.batch.params.BatchConsumerParam;
import com.relaxed.common.core.batch.params.BatchExceptionParam;
import com.relaxed.common.core.batch.params.BatchGroup;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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
	 * 任务名称
	 */
	private String taskName = "default";

	/**
	 * 分组参数
	 */
	private BatchGroup batchGroup;

	/**
	 * 批处理消费者
	 */
	private BatchConsumer batchConsumer;

	/**
	 * 批处理数据获取函数
	 */
	private BatchSupplier batchSupplier;

	/**
	 * 错误处理器 默认什么也不做
	 */
	private Consumer<BatchExceptionParam> errorHandler = batchExceptionParam -> {
	};

	/**
	 * 是否开启异步
	 */
	private boolean async = false;

	public AbstractBatchOps(BatchGroup batchGroup, BatchConsumer batchConsumer, BatchSupplier batchSupplier) {
		this.batchGroup = batchGroup;
		this.batchConsumer = batchConsumer;
		this.batchSupplier = batchSupplier;
	}

	public AbstractBatchOps(String taskName, BatchGroup batchGroup, BatchConsumer batchConsumer,
			BatchSupplier batchSupplier) {
		this.taskName = taskName;
		this.batchGroup = batchGroup;
		this.batchConsumer = batchConsumer;
		this.batchSupplier = batchSupplier;
	}

	/**
	 * 运行批处理
	 */
	public void runBatch() {
		log.info("{}-batch process start...", taskName);
		StopWatch stopWatch = new StopWatch(taskName);
		stopWatch.start();
		Integer groupNum = batchGroup.getGroupNum();
		int total = batchGroup.getTotal();
		List<CompletableFuture> completableFutures = new ArrayList<>(groupNum);
		for (int i = 1; i <= groupNum; i++) {
			// 起始处理值
			int startNum = computeStartPos(i);
			if (async) {
				completableFutures.add(runAsync(startNum, () -> process(total, startNum)));
			}
			else {
				process(total, startNum);
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
	 * @param total 总数
	 * @param startNum 起始pos
	 */
	protected void process(int total, int startNum) {
		// 获取数据
		List list = batchSupplier.apply(startNum, total);
		// 构造消费者入参
		BatchConsumerParam consumerData = buildConsumerParam(startNum, list);
		// 消费消费者
		batchConsumer.consumer(consumerData);
	}

	/**
	 * 计算起始位置
	 * @param currentIndex
	 * @return
	 */
	protected int computeStartPos(int currentIndex) {
		return (currentIndex - 1) * batchGroup.getSize();
	}

	/**
	 * 构建批处理消费者参数
	 * @param startNum
	 * @param list
	 * @return
	 */
	protected BatchConsumerParam buildConsumerParam(Integer startNum, List list) {
		BatchConsumerParam consumerData = new BatchConsumerParam<>();
		consumerData.setBatchGroup(batchGroup);
		consumerData.setCurrentStepPosition(startNum);
		consumerData.setData(list);
		return consumerData;
	}

	/**
	 * 获取线程执行器
	 * @return
	 */
	protected abstract ThreadPoolExecutor executor();

	/**
	 * 无返回值异步
	 * @param currentStepPosition
	 * @param runnable
	 * @return
	 */
	private CompletableFuture<Void> runAsync(Integer currentStepPosition, Runnable runnable) {
		return runAsync(currentStepPosition, runnable, errorHandler);
	}

	/**
	 * 有返回值异步
	 * @param currentStepPosition
	 * @param supplier
	 * @param <U>
	 * @return
	 */
	private <U> CompletableFuture<U> supplyAsync(Integer currentStepPosition, Supplier<U> supplier) {
		return supplyAsync(currentStepPosition, supplier, errorHandler);
	}

	/**
	 * 无返回值异步
	 * @param currentStepPosition
	 * @param runnable
	 * @param errorHandler
	 * @return
	 */
	private CompletableFuture<Void> runAsync(Integer currentStepPosition, Runnable runnable,
			Consumer<BatchExceptionParam> errorHandler) {
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
	 * @param supplier
	 * @param errorHandler
	 * @param <U>
	 * @return
	 */
	private <U> CompletableFuture<U> supplyAsync(Integer currentStepPosition, Supplier<U> supplier,
			Consumer<BatchExceptionParam> errorHandler) {
		return CompletableFuture.supplyAsync(supplier, executor()).exceptionally(throwable -> {
			BatchExceptionParam batchExceptionParam = BatchExceptionParam.of(taskName, currentStepPosition, batchGroup,
					throwable);
			errorHandler.accept(batchExceptionParam);
			return null;
		});
	}

}

package com.relaxed.common.core.batch;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Yakir
 * @Topic BatchUtil
 * @Description
 * @date 2023/4/3 14:50
 * @Version 1.0
 */
@Slf4j
public class BatchUtil {

	/**
	 * 批次配置
	 */
	private BatchConfig config;

	/**
	 * 批次坐标定位器
	 */
	private LocationComputer locationComputer = (currentIndex, size) -> (currentIndex - 1) * size;

	public static BatchUtil ops() {
		return new BatchUtil();
	}

	public BatchConfig config() {
		this.config = new BatchConfig(this);
		return config;
	}

	public BatchUtil locationComputer(LocationComputer locationComputer) {
		this.locationComputer = locationComputer;
		return this;
	}

	public <T extends BatchLocation, M> void run(BatchGroup group, ITask<T, M> task) {
		// 任务名称
		String taskName = task.taskName();
		boolean async = task.isAsync();
		// 获取执行器
		Executor executor = config.getExecutor();
		// 任务组相关
		int size = group.getSize();
		long groupNum = group.getGroupNum();
		log.info("{}-batch process start...", taskName);
		StopWatch stopWatch = new StopWatch(taskName);
		stopWatch.start();
		List<CompletableFuture> completableFutures = new ArrayList<>();
		// 开启线程安全监测 限制最大线程数 防止线程全部占用满
		int limitMaxThread = config.getLimitMaxThread();
		boolean isFull = false;
		// 遍历组
		try {
			task.init();
			for (int i = 1; i <= groupNum; i++) {
				// 批次起始索引坐标
				int batchStartIndex = this.locationComputer.compute(i, size);
				T batchLocation = (T) new BatchLocation().setBatchStartPos(batchStartIndex).setBatchSize(size);
				if (async) {
					// 异步
					CompletableFuture<Void> runAsync = CompletableFuture.runAsync(() -> process(task, batchLocation),
							executor);
					completableFutures.add(runAsync);
					if (config.isThreadSafeCheck() && completableFutures.size() >= limitMaxThread) {
						// 开始线程监测,并分组数大于限制值
						isFull = true;
					}
					if (isFull) {
						if (CollectionUtil.isNotEmpty(completableFutures)) {
							CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).join();
							// completableFutures.forEach(CompletableFuture::join);
							completableFutures.clear();
							isFull = false;
						}
					}

				}
				else {
					// 同步
					process(task, batchLocation);
				}

			}
			// 异步任务时候 同步等待执行完成
			if (CollectionUtil.isNotEmpty(completableFutures)) {
				// completableFutures.forEach(CompletableFuture::join);
				CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).join();

			}
		}
		finally {
			task.releaseResources();
		}

		stopWatch.stop();
		log.info("{}-batch process end time {} ms...", taskName, stopWatch.getTotalTimeMillis());
	}

	private static <T extends BatchLocation, M> void process(ITask<T, M> task, T batchLocation) {

		// 提供者
		ITask.BatchSupplier<T, M> supplier = task.supplier();
		// 获取消费者
		ITask.BatchConsumer<T, M> consumer = task.consumer();
		List<M> dataList = null;
		try {
			dataList = supplier.get(batchLocation);
		}
		catch (Exception e) {
			task.exceptionCollect(batchLocation, null, e);
		}
		if (dataList == null) {
			return;
		}
		int currentBatchSize = Math.min(dataList.size(), batchLocation.getBatchSize());
		for (int j = 0; j < currentBatchSize; j++) {
			M data = dataList.get(j);
			int innerPosStartIndex = j + 1;
			batchLocation.setCurrent(batchLocation.getBatchStartPos() + innerPosStartIndex);
			try {
				consumer.consumer(batchLocation, data);
			}
			catch (Exception e) {
				task.exceptionCollect(batchLocation, data, e);
			}
		}

	}

	public interface LocationComputer {

		/**
		 * 批次起始坐标计算
		 * @param currentIndex
		 * @param size
		 * @return
		 */
		int compute(int currentIndex, int size);

	}

}

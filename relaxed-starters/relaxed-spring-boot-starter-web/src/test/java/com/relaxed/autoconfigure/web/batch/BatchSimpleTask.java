package com.relaxed.autoconfigure.web.batch;

import com.relaxed.common.core.batch.BatchLocation;
import com.relaxed.common.core.batch.ITask;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Yakir
 * @Topic BatchLoanTask
 * @Description
 * @date 2023/4/4 16:30
 * @Version 1.0
 */
@Slf4j
public class BatchSimpleTask implements ITask<BatchLocation, String> {

	private AtomicInteger count = new AtomicInteger(0);

	@Override
	public String taskName() {
		return "测试任务";
	}

	@Override
	public boolean isAsync() {
		return true;
	}

	@Override
	public BatchSupplier<BatchLocation, String> supplier() {
		return new BatchSupplier<BatchLocation, String>() {
			@Override
			public List<String> get(BatchLocation batchLocation) {
				int batchStartPos = batchLocation.getBatchStartPos();
				int batchSize = batchLocation.getBatchSize();
				List<String> dataalist = new ArrayList<>();
				for (int i = 0; i < batchSize; i++) {
					dataalist.add(batchStartPos * i + "");
				}
				log.info("生产者坐标{},生成数据{}", batchLocation, dataalist);
				return dataalist;
			}
		};
	}

	@Override
	public BatchConsumer<BatchLocation, String> consumer() {
		return new BatchConsumer<BatchLocation, String>() {
			@Override
			public void consumer(BatchLocation batchLocation, String data) {
				log.info("消费者坐标{},数据{}", batchLocation, data);
				int current = batchLocation.getCurrent();
				try {
					Thread.sleep((long) (Math.random() * 1000L));
				}
				catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				if (current == 10 || current == 20) {
					throw new IllegalArgumentException("invalid batch location");
				}
			}
		};
	}

	@Override
	public void exceptionCollect(BatchLocation extra, String data, Throwable throwable) {
		log.info("当前定位处理异常,坐标{},数据{}", extra, data, throwable);
		count.incrementAndGet();
		// throw new IllegalArgumentException("invalid batch location");
	}

	public AtomicInteger getCount() {
		return count;
	}

}

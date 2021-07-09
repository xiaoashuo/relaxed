package com.relaxed.common.core.batch;

import cn.hutool.core.thread.ThreadUtil;
import com.relaxed.common.core.batch.functions.BatchConsumer;
import com.relaxed.common.core.batch.functions.BatchSupplier;
import com.relaxed.common.core.batch.params.BatchConsumerParam;
import com.relaxed.common.core.batch.params.BatchGroup;
import com.relaxed.common.core.exception.BusinessException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author Yakir
 * @Topic BatchOps
 * @Description
 * @date 2021/7/9 18:35
 * @Version 1.0
 */
public class BatchOps extends AbstractBatchOps {

	public BatchOps(BatchGroup batchGroup, BatchConsumer batchConsumer, BatchSupplier batchSupplier) {
		super(batchGroup, batchConsumer, batchSupplier);
	}

	public BatchOps(String taskName, BatchGroup batchGroup, BatchConsumer batchConsumer, BatchSupplier batchSupplier) {
		super(taskName, batchGroup, batchConsumer, batchSupplier);
	}

	/**
	 * 核心线程池大小
	 */
	private static int coreSize = Runtime.getRuntime().availableProcessors();

	/**
	 * 最大线程池大小
	 */
	private static int maxSize = coreSize * 2 + 1;

	/**
	 * 执行器
	 * @return
	 */
	@Override
	protected ThreadPoolExecutor executor() {
		return ThreadUtil.newExecutor(coreSize, maxSize);
	}

	public static void main(String[] args) {
		int externalNum = 168;
		BatchGroup batchGroup = new BatchGroup(100, 500);
		BatchConsumer batchConsumer = data -> {
			System.out.println("开始消费" + data + "当前线程id" + Thread.currentThread());
			BatchConsumerParam batchConsumerParam = data;
			int currentStepPosition = batchConsumerParam.getCurrentStepPosition();
			if (currentStepPosition == 99500) {
				throw new BusinessException(100, "消费出现异常");
			}
			List<String> data1 = batchConsumerParam.getData();
			long count = data1.stream().collect(Collectors.summingLong(e -> Long.parseLong(e)));
		};
		BatchSupplier<String> batchSupplier = (startNum, total) -> {
			System.out.println("起始步进开始值" + startNum + "总数目" + total + "外部参数" + externalNum);
			List<String> list = new ArrayList<>();
			list.add("1");
			list.add("2");
			return list;
		};
		BatchOps abstractBatchOps = new BatchOps(batchGroup, batchConsumer, batchSupplier);
		abstractBatchOps.setTaskName("合同盖章");

		// abstractBatchOps.setErrorHandler((batchExceptionParam -> {
		// System.out.println("错误发送"+batchExceptionParam);
		// }))
		abstractBatchOps.setAsync(true);
		abstractBatchOps.runBatch();

	}

}

package com.relaxed.autoconfigure.web.batch;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import com.relaxed.common.core.batch.functions.BatchConsumer;
import com.relaxed.common.core.batch.functions.BatchSupplier;
import com.relaxed.common.core.batch.functions.ExceptionHandler;
import com.relaxed.common.core.batch.params.BatchGroup;
import com.relaxed.common.core.batch.params.BatchParam;
import com.relaxed.common.core.batch.params.DataWrapper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Yakir
 * @Topic BatchTest
 * @Description
 * @date 2022/6/5 17:43
 * @Version 1.0
 */
@Slf4j
public class BatchTest {

	public static void main(String[] args) {

		int total = 1000;
		int pageSize = 20;
		List<String> dataList = new ArrayList<>();
		for (int i = 0; i < total; i++) {
			dataList.add("data-" + i);
		}

		AtomicInteger exceptionNum = new AtomicInteger();
		// 1.定义分组参数
		BatchGroup batchGroup = new BatchGroup(total, pageSize);
		// 2.定义提取数据方式
		BatchSupplier<String> supplier = (currentStepPosition, size) -> {
			ThreadUtil.sleep(1000);
			return ListUtil.sub(dataList, currentStepPosition, currentStepPosition + size);
		};
		// 3.定义数据消费方式
		BatchConsumer<String> consumer = new BatchConsumer<String>() {
			@Override
			public void consumer(DataWrapper<String> data) {
				System.out.println("当前线程" + Thread.currentThread().getName() + "--" + JSONUtil.toJsonStr(data));
				int currentPosIndex = data.getCurrentPosIndex();
				if (currentPosIndex % 20 == 0) {
					exceptionNum.incrementAndGet();
					throw new IllegalArgumentException("消费异常");

				}
			}
		};
		ExceptionHandler<String> exceptionHandler = new ExceptionHandler<String>() {
			@Override
			public void collect(String taskName, DataWrapper<String> dataWrapper, Throwable throwable) {
				log.error("当前线程" + Thread.currentThread().getName() + "--名称--" + taskName + "--"
						+ JSONUtil.toJsonStr(dataWrapper), throwable);
			}
		};
		// 4.填充批次参数
		BatchParam batchParam = BatchParam.ofRun(batchGroup, supplier, consumer);
		batchParam.setTaskName("测试数据");
		batchParam.setAsync(true);
		batchParam.setExceptionHandler(exceptionHandler);
		// 5.创建批次执行者 并运行
		BatchOps batchOps = new BatchOps();
		batchOps.runBatch(batchParam);
		System.out.println(exceptionNum);
	}

}

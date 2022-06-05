package com.relaxed.autoconfigure.web.batch;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.relaxed.common.core.batch.functions.BatchConsumer;
import com.relaxed.common.core.batch.functions.BatchSupplier;
import com.relaxed.common.core.batch.params.BatchGroup;
import com.relaxed.common.core.batch.params.BatchParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yakir
 * @Topic BatchTest
 * @Description
 * @date 2022/6/5 17:43
 * @Version 1.0
 */
public class BatchTest {

	public static void main(String[] args) {

		int total = 10000;
		int pageSize = 20;
		List<String> dataList = new ArrayList<>();
		for (int i = 0; i < total; i++) {
			dataList.add("data-" + i);
		}
		// 1.定义分组参数
		BatchGroup batchGroup = new BatchGroup(total, pageSize);
		// 2.定义提取数据方式
		BatchSupplier<String> supplier = (currentStepPosition, size) -> {
			ThreadUtil.sleep(3000);
			return ListUtil.sub(dataList, currentStepPosition, currentStepPosition + size);
		};
		// 3.定义数据消费方式
		BatchConsumer<String> consumer = data -> System.out
				.println("当前线程" + Thread.currentThread().getName() + "--" + data);
		// 4.填充批次参数
		BatchParam batchParam = BatchParam.ofRun(batchGroup, supplier, consumer);
		batchParam.setTaskName("测试数据");
		batchParam.setAsync(false);
		// 5.创建批次执行者 并运行
		BatchOps batchOps = new BatchOps();
		batchOps.runBatch(batchParam);
	}

}

package com.relaxed.autoconfigure.web.batch;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.relaxed.common.core.batch.BatchExec;
import com.relaxed.common.core.batch.base.BatchMeta;
import com.relaxed.common.core.batch.base.DataConsumer;
import com.relaxed.common.core.batch.base.DataProvider;
import com.relaxed.common.core.batch.base.ExceptionHandler;
import com.relaxed.common.core.batch.base.Step;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Yakir
 * @Topic BatchTest
 * @Description
 * @date 2023/4/4 16:45
 * @Version 1.0
 */
@Slf4j
public class BatchTest {

	public static void main(String[] args) {

		int batchSize = 200;
		List<UserEntity> dataList = getDataList(batchSize);
		AtomicInteger count = new AtomicInteger(0);
		Step<UserEntity> step = new Step<UserEntity>().async(true).batch(batchSize, 10).taskName("测试批处理任务")
				.supplier(batchMeta -> {
					Integer startIndex = batchMeta.getStartIndex();
					// if (batchMeta.getGroupNo() == 2) {
					// throw new RuntimeException("mock 异常");
					// }
					return ListUtil.sub(dataList, startIndex, startIndex + batchMeta.getSize());
				}).consumer((batchMeta, innerRow, data) -> {
					// System.out.println(StrUtil.format("当前线程{}分组编号{},批次索引{},大小{},内部行号{},数据{}",
					// Thread.currentThread().getName(), batchMeta.getGroupNo(),
					// batchMeta.getStartIndex(),
					// batchMeta.getSize(), innerRow, data));
					ThreadUtil.sleep(100);
					if (innerRow % 5 == 0) {
						throw new RuntimeException("mock 消费异常");
					}
				}).exp(throwable -> {
					count.incrementAndGet();
					// throw new IllegalArgumentException("invalid batch location");
				});
		new BatchExec(step).exec();
		log.info("任务报错数目:" + count.get());
		ThreadUtil.sleep(200000);
	}

	private static List<UserEntity> getDataList(int batchSize) {
		List<UserEntity> dataList = new ArrayList<>();
		for (int i = 0; i < batchSize; i++) {
			UserEntity userEntity = new UserEntity();
			userEntity.setIndex(i + 1);
			userEntity.setUsername("姓" + i);
			userEntity.setPassword("123456");
			dataList.add(userEntity);
		}
		return dataList;
	}

}

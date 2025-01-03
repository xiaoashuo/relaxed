package com.relaxed.test.utils.batch;

import cn.hutool.core.thread.NamedThreadFactory;
import com.relaxed.common.core.util.batch.BatchExecutor;
import com.relaxed.common.core.util.batch.core.BatchMeta;
import com.relaxed.common.core.util.batch.funcs.DataConsumer;
import com.relaxed.common.core.util.batch.funcs.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Yakir
 * @Topic BatchTest
 * @Description
 * @date 2025/1/2 18:24
 * @Version 1.0
 */
public class BatchTest {

	public static void main(String[] args) {
		ThreadPoolExecutor poolExecutor = getExecutor();

		AtomicInteger expNum = new AtomicInteger(0);
		try {

			// 构造任务
			BatchExecutor.create().executor(poolExecutor).props().taskName("测试").async(true).end().params()
					.totalCount(1000).size(100).end().<TestModel>task().provider(batchMeta -> {
						Integer startIndex = batchMeta.getStartIndex();
						Integer groupNo = batchMeta.getGroupNo();
						System.out.println("组数" + groupNo + "-" + startIndex);
						List<TestModel> datas = new ArrayList<>();
						for (int i = 0; i < 100; i++) {
							TestModel testModel = new TestModel();
							testModel.setUsername("tsname" + i);
							datas.add(testModel);
						}
						return datas;
					}).consumer(new DataConsumer<TestModel>() {
						@Override
						public void accept(BatchMeta batchMeta, Integer innerRow, TestModel data) {
							System.out.println("当前接收到数据:" + data.toString());
							if (innerRow == 10) {
								throw new RuntimeException("mock 消费异常");
							}
						}
					}).expResolver(new ExceptionHandler() {
						@Override
						public void handle(BatchMeta meta, Map<String, Object> extParam, Throwable throwable) {
							expNum.incrementAndGet();
						}
					}).end().execute();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		System.out.println(String.format("批次执行结果:%s", expNum));

	}

	private static ThreadPoolExecutor getExecutor() {
		ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 10, 3000, TimeUnit.SECONDS,
				new ArrayBlockingQueue<>(100));
		poolExecutor.setThreadFactory(new NamedThreadFactory("relaxed:batch:", false));
		return poolExecutor;
	}

}

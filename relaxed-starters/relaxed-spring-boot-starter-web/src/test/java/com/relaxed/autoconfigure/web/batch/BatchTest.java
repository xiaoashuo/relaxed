package com.relaxed.autoconfigure.web.batch;

import cn.hutool.core.thread.ThreadUtil;
import com.relaxed.common.core.batch.BatchGroup;
import com.relaxed.common.core.batch.BatchUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

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

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(4);
		executor.setQueueCapacity(2);
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
		executor.initialize();
		BatchUtil batchUtil = BatchUtil.ops().config().executor(executor).threadSafeCheck(true).limitMaxThread(2).end();
		BatchSimpleTask task = new BatchSimpleTask();

		batchUtil.run(new BatchGroup(100, 1), task);
		log.info("任务报错数目:" + task.getCount().get());
		ThreadUtil.sleep(200000);
	}

}

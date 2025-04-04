package com.relaxed.test.thread;

import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.core.thread.ThreadUtil;
import com.relaxed.common.jsch.sftp.SftpAutoConfiguration;
import com.relaxed.pool.monitor.ThreadPoolMonitorAutoConfiguration;
import com.relaxed.pool.monitor.annotation.ThreadPoolMonitor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Yakir
 * @Topic ThreadPoolMonitorApplicationTest
 * @Description
 * @date 2025/4/4 11:22
 * @Version 1.0
 */
@Slf4j
@SpringBootTest(classes = { ThreadPoolConfig.class, ThreadPoolMonitorAutoConfiguration.class },
		properties = "spring.config.location=classpath:/thread/application-thread.yml")
public class ThreadPoolMonitorApplicationTest {

	@Autowired
	ThreadPoolExecutor testThreadPool;

	@Test
	void test() {
		for (int i = 0; i < 1000; i++) {
			PoolOrder poolOrder = new PoolOrder();
			poolOrder.setUsername("username" + i);
			processOrderAsync(poolOrder, testThreadPool);
		}
		ThreadUtil.sleep(70000);
		List<CompletableFuture> jobs = new ArrayList<>();
		for (int i = 0; i < 1000; i++) {
			PoolOrder poolOrder = new PoolOrder();
			poolOrder.setUsername("username" + i);
			jobs.add(processOrderAsync(poolOrder, testThreadPool));
		}
		CompletableFuture.allOf(jobs.toArray(new CompletableFuture[0])).join();
		ThreadUtil.safeSleep(80000);
	}

	// 业务方法
	public static CompletableFuture<String> processOrderAsync(PoolOrder order,
			ThreadPoolExecutor orderProcessExecutor) {
		return CompletableFuture.supplyAsync(() -> {
			// 处理订单逻辑
			return doProcessOrder(order);
		}, orderProcessExecutor);
	}

	private static String doProcessOrder(PoolOrder order) {
		System.out.println("当前处理到order" + Thread.currentThread().getName());
		ThreadUtil.safeSleep(3000);
		return "success";
	}

}

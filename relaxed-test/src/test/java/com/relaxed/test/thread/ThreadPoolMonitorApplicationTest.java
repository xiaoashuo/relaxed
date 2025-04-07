package com.relaxed.test.thread;

import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.core.thread.ThreadUtil;
import com.relaxed.common.jsch.sftp.SftpAutoConfiguration;
import com.relaxed.pool.monitor.ThreadPoolMonitorAutoConfiguration;
import com.relaxed.pool.monitor.annotation.ThreadPoolMonitor;
import com.relaxed.pool.monitor.monitor.ThreadPoolStats;
import com.relaxed.pool.monitor.monitor.ThreadPoolTaskMonitor;
import com.relaxed.pool.monitor.monitor.ThreadPoolTrend;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
@SpringBootTest(
		classes = { ThreadPoolConfig.class, ThreadPoolController.class, ThreadPoolMonitorAutoConfiguration.class },
		properties = "spring.config.location=classpath:/thread/application-thread.yml")
public class ThreadPoolMonitorApplicationTest {

	@Autowired
	ThreadPoolExecutor testThreadPool;

	@Autowired
	ThreadPoolController threadPoolController;

	@Resource
	ThreadPoolTaskExecutor sealTaskExecutor;

	@Test
	void test() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					List<ThreadPoolStats> allStats = threadPoolController.getAllStats();
					Map<String, ThreadPoolTrend> allTrends = threadPoolController.getAllTrends();
					ThreadUtil.sleep(1000);
				}
			}
		}).start();
		for (int i = 0; i < 1000; i++) {
			PoolOrder poolOrder = new PoolOrder();
			poolOrder.setUsername("username" + i);
			processOrderAsync(poolOrder, sealTaskExecutor);
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

	public static CompletableFuture<String> processOrderAsync(PoolOrder order,
			ThreadPoolTaskExecutor threadPoolTaskExecutor) {
		return CompletableFuture.supplyAsync(() -> {
			// 处理订单逻辑
			return doProcessOrder(order);
		}, threadPoolTaskExecutor);
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

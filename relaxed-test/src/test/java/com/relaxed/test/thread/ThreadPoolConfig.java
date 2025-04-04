package com.relaxed.test.thread;

import cn.hutool.core.thread.NamedThreadFactory;
import com.relaxed.pool.monitor.annotation.ThreadPoolMonitor;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Yakir
 * @Topic ThreadPoolConfig
 * @Description
 * @date 2025/4/4 11:34
 * @Version 1.0
 */
public class ThreadPoolConfig {

	@ThreadPoolMonitor(name = "测试线程池")
	@Bean
	public ThreadPoolExecutor testThreadPool() {
		ThreadPoolExecutor orderProcessExecutor = new ThreadPoolExecutor(3, // 核心线程数
				5, // 最大线程数
				60, TimeUnit.SECONDS, // 空闲线程存活时间
				new LinkedBlockingQueue<>(200), // 工作队列
				new NamedThreadFactory("order-process-", false), new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略
		);
		return orderProcessExecutor;
	}

	@ThreadPoolMonitor
	@Bean
	public ThreadPoolExecutor backThreadPool() {
		ThreadPoolExecutor orderProcessExecutor = new ThreadPoolExecutor(3, // 核心线程数
				5, // 最大线程数
				60, TimeUnit.SECONDS, // 空闲线程存活时间
				new LinkedBlockingQueue<>(200), // 工作队列
				new NamedThreadFactory("back-process-", false), new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略
		);
		return orderProcessExecutor;
	}

}

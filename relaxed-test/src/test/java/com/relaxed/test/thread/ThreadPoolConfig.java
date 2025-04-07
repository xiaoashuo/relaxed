package com.relaxed.test.thread;

import cn.hutool.core.thread.NamedThreadFactory;
import com.relaxed.pool.monitor.annotation.ThreadPoolMonitor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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

	@ThreadPoolMonitor(name = "签章线程池")
	@Bean
	public ThreadPoolTaskExecutor sealTaskExecutor(ThreadPoolExecutor testThreadPool) {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// 核心线程数4：线程池创建时候初始化的线程数
		executor.setCorePoolSize(4);
		// 最大线程数10：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
		executor.setMaxPoolSize(10);
		// 缓冲队列100：用来缓冲执行任务的队列
		executor.setQueueCapacity(200);
		// ****重要***
		// 允许线程的空闲时间60秒：当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
		executor.setKeepAliveSeconds(60);
		// 线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
		executor.setThreadNamePrefix("seal-");
		// 上下级线程间透传上下文，用于标记追踪日志或调用链路
		// executor.setTaskDecorator(new MdcTaskDecorator());
		// 设置拒绝策略 由当前（主）线程执行
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		// executor.setTaskDecorator(mdcTaskDecorator);
		executor.initialize();
		return executor;
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

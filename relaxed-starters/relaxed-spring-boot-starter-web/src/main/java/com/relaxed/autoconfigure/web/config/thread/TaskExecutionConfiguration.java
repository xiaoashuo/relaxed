package com.relaxed.autoconfigure.web.config.thread;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 任务执行配置类 用于配置线程池和任务装饰器 支持MDC上下文传递和自定义线程池策略
 *
 * @author Yakir
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
public class TaskExecutionConfiguration {

	/**
	 * 配置MDC任务装饰器 用于在线程池中传递MDC上下文信息 确保异步任务能够正确获取和传递日志上下文
	 * @return MDC任务装饰器实例
	 */
	@Bean
	@ConditionalOnMissingBean
	public TaskDecorator mdcTaskDecorator() {
		return new MdcTaskDecorator();
	}

	/**
	 * 配置线程池自定义器 设置线程池的拒绝策略为CallerRunsPolicy 当线程池无法处理新任务时，由调用者线程执行该任务
	 * @return 线程池自定义器实例
	 */
	@Bean
	@ConditionalOnMissingBean
	public TaskExecutorCustomizer taskExecutorCustomizerExtend() {
		return (executor) -> {
			// 线程池对拒绝任务(无线程可用)的处理策略
			executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		};
	}

}

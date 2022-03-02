package com.relaxed.admin.core.thread;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Yakir
 * @Topic TaskExecutionConfiguration
 * @Description
 * @date 2022/2/25 10:34
 * @Version 1.0
 */
@Configuration(proxyBeanMethods = false)
public class TaskExecutionConfiguration {

	/**
	 * mdc 任务解码器
	 * @author yakir
	 * @date 2021/9/18 9:35
	 * @return org.springframework.core.task.TaskDecorator
	 */
	@Bean
	@ConditionalOnMissingBean
	public TaskDecorator mdcTaskDecorator() {
		return new MdcTaskDecorator();
	}

	/**
	 * 线程池自定义
	 * @author yakir
	 * @date 2021/9/18 9:35
	 * @return org.springframework.boot.task.TaskExecutorCustomizer
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

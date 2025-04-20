package com.relaxed.autoconfigure.log;

import com.relaxed.autoconfigure.log.properties.LogProperties;
import com.relaxed.common.log.operation.aspect.OperationLogAspect;
import com.relaxed.common.log.operation.service.OperationLogHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 操作日志自动配置类。 用于配置操作日志的切面和处理器。 主要功能包括： 1. 根据配置决定是否启用操作日志 2. 注册操作日志切面 3. 支持自定义操作日志处理器
 *
 * @author Yakir
 * @since 1.0
 */
@Configuration
@ConditionalOnProperty(prefix = LogProperties.Operation.PREFIX, name = "enabled", havingValue = "true")
public class OperationLogAutoConfiguration {

	/**
	 * 注册操作日志切面。 当存在 OperationLogHandler 实例时，自动注册切面。
	 * @param operationLogHandler 操作日志处理器
	 * @param <T> 操作日志实体类型
	 * @return 操作日志切面实例
	 */
	@Bean
	@ConditionalOnBean(OperationLogHandler.class)
	public <T> OperationLogAspect<T> operationLogAspect(OperationLogHandler<T> operationLogHandler) {
		return new OperationLogAspect<>(operationLogHandler);
	}

}

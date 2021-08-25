package com.relaxed.common.log.operation;

import com.relaxed.common.log.operation.aspect.OperationLogAspect;
import com.relaxed.common.log.operation.service.OperationLogHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

/**
 * @author Yakir
 * @Topic OperationLogAutoConfiguration
 * @Description
 * @date 2021/6/27 12:20
 * @Version 1.0
 */
public class OperationLogAutoConfiguration {

	/**
	 * 注册操作日志Aspect
	 * @return OperationLogAspect
	 */
	@Bean
	@ConditionalOnBean(OperationLogHandler.class)
	public <T> OperationLogAspect<T> operationLogAspect(OperationLogHandler<T> operationLogHandler) {
		return new OperationLogAspect<>(operationLogHandler);
	}

}

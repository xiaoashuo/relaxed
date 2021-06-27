package com.relaxed.common.log.operation;

import com.relaxed.common.log.operation.annotation.CreateLog;
import com.relaxed.common.log.operation.aspect.OperationLogAspect;
import com.relaxed.common.log.operation.enums.LogStatusEnum;
import com.relaxed.common.log.operation.event.OperationLogListener;
import com.relaxed.common.log.operation.service.OperationLogHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationEventPublisher;
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
	 * 注册日志保存事件监听器
	 * @return OperationLogListener
	 */
	@Bean
	@ConditionalOnBean(OperationLogHandler.class)
	public OperationLogListener operationLogListener(OperationLogHandler operationLogHandler) {
		return new OperationLogListener(operationLogHandler);
	}

	/**
	 * 注册操作日志Aspect
	 * @return OperationLogAspect
	 */
	@Bean
	@ConditionalOnBean(OperationLogHandler.class)
	public OperationLogAspect operationLogAspect(ApplicationEventPublisher publisher) {
		return new OperationLogAspect(publisher);
	}

}

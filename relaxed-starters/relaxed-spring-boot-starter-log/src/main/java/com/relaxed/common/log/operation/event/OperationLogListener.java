package com.relaxed.common.log.operation.event;

import com.relaxed.common.log.operation.service.OperationLogHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

/**
 * @author Yakir
 * @Topic LogListener
 * @Description
 * @date 2021/6/27 12:59
 * @Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
public class OperationLogListener {

	private final OperationLogHandler operationLogHandler;

	@Async
	@Order
	@EventListener(OperationLogEvent.class)
	public void saveSysLog(OperationLogEvent event) {
		operationLogHandler.saveLog(event.getOperationLogDTO());
	}

}

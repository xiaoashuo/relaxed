package com.relaxed.common.log.operation.event;

import com.relaxed.common.log.operation.model.OperationLogInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Yakir
 * @Topic LogEvent
 * @Description
 * @date 2021/6/27 12:57
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public class OperationLogEvent {

	private final OperationLogInfo operationLogInfo;

}

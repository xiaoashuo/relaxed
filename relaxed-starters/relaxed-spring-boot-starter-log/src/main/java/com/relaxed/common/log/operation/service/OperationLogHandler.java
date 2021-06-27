package com.relaxed.common.log.operation.service;

import com.relaxed.common.log.operation.model.OperationLogDTO;

/**
 * @author Yakir
 * @Topic LogHandler
 * @Description
 * @date 2021/6/27 12:58
 * @Version 1.0
 */
public interface OperationLogHandler {

	/**
	 * 存储日志
	 * @param operationLogDTO
	 */
	void saveLog(OperationLogDTO operationLogDTO);

}

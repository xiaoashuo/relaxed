package com.relaxed.common.log.operation.service;

import com.relaxed.common.log.operation.annotation.Log;
import com.relaxed.common.log.operation.model.OperationLogInfo;
import org.aspectj.lang.ProceedingJoinPoint;

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
	 * @param operationLogInfo
	 */
	void saveLog(OperationLogInfo operationLogInfo);

	/**
	 * 创建操作日志
	 * @param operationLogging 操作日志注解
	 * @param joinPoint 当前执行方法的切点信息
	 * @param executionTime 方法执行时长
	 * @param throwable 方法执行的异常，为 null 则表示无异常
	 * @return T 操作日志对象
	 */
	OperationLogInfo createOperationLog(Log operationLogging, ProceedingJoinPoint joinPoint, long executionTime,
			Throwable throwable);

}

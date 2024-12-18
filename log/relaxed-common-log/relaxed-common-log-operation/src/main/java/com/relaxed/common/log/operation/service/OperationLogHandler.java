package com.relaxed.common.log.operation.service;

import com.relaxed.common.log.operation.annotation.Log;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author Yakir
 * @Topic LogHandler
 * @Description
 * @date 2021/6/27 12:58
 * @Version 1.0
 */
public interface OperationLogHandler<T> {

	/**
	 * 创建操作日志
	 * @param operationLogging 操作日志注解
	 * @param joinPoint 当前执行方法的切点信息
	 * @return T 操作日志对象
	 */
	T buildLog(Log operationLogging, ProceedingJoinPoint joinPoint);

	/**
	 * 目标方法执行完成后进行信息补充记录, 如执行时长，异常信息，还可以通过切点记录返回值，如果需要的话
	 * @param log 操作日志对象 {@link #buildLog}
	 * @param joinPoint 当前执行方法的切点信息
	 * @param startTime 方法开始时间
	 * @param endTime 方法结束时间
	 * @param throwable 方法执行的异常，为 null 则表示无异常
	 * @param isSaveResult 是否存储结果
	 * @param executionResult 执行结果 可能为null
	 * @return 操作日志对象
	 */
	T fillExecutionInfo(T log, ProceedingJoinPoint joinPoint, long startTime, long endTime, Throwable throwable,
			boolean isSaveResult, Object executionResult);

	/**
	 * 处理日志，可以在这里进行存储，或者输出
	 * @param operationLog 操作日志
	 */
	void handleLog(T operationLog);

}

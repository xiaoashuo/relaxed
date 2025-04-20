package com.relaxed.common.log.operation.service;

import com.relaxed.common.log.operation.annotation.Log;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 操作日志处理器接口，定义了操作日志的生命周期管理方法。 该接口负责操作日志的创建、信息补充和最终处理，支持自定义日志实体类型。
 * 实现类可以根据具体需求选择日志的存储方式（如数据库、文件、消息队列等）。
 *
 * @author Yakir
 * @param <T> 操作日志实体类型
 * @since 1.0.0
 */
public interface OperationLogHandler<T> {

	/**
	 * 创建操作日志实体对象。 根据注解信息和切点信息构建初始的日志对象，通常包括： - 操作类型 - 操作描述 - 操作人信息 - 请求参数（如果需要记录） -
	 * 其他基础信息
	 * @param operationLogging 操作日志注解，包含日志配置信息
	 * @param joinPoint 切点信息，包含方法的调用细节
	 * @return 创建的操作日志对象
	 */
	T buildLog(Log operationLogging, ProceedingJoinPoint joinPoint);

	/**
	 * 补充方法执行的相关信息。 在目标方法执行完成后调用，用于填充执行过程中产生的信息，包括： - 执行时长 - 执行状态 - 异常信息（如果有） -
	 * 返回结果（如果配置了记录） - 其他运行时信息
	 * @param log 待补充信息的日志对象，由 {@link #buildLog} 方法创建
	 * @param joinPoint 切点信息，包含方法执行的上下文
	 * @param startTime 方法开始执行的时间戳
	 * @param endTime 方法结束执行的时间戳
	 * @param throwable 执行过程中的异常，如无异常则为 null
	 * @param isSaveResult 是否需要保存执行结果
	 * @param executionResult 方法执行的返回值，可能为 null
	 * @return 补充完成的操作日志对象
	 */
	T fillExecutionInfo(T log, ProceedingJoinPoint joinPoint, long startTime, long endTime, Throwable throwable,
			boolean isSaveResult, Object executionResult);

	/**
	 * 处理完整的操作日志。 该方法在日志信息收集完成后调用，负责日志的最终处理，可以： - 保存到数据库 - 写入日志文件 - 发送到消息队列 - 输出到控制台 -
	 * 或者进行其他自定义处理
	 * @param operationLog 完整的操作日志对象
	 */
	void handleLog(T operationLog);

}

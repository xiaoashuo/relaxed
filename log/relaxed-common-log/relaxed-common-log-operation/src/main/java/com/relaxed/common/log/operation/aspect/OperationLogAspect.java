package com.relaxed.common.log.operation.aspect;

import com.relaxed.common.log.operation.annotation.Log;
import com.relaxed.common.log.operation.service.OperationLogHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;

import java.lang.reflect.Method;

/**
 * 操作日志切面，用于处理通过 {@link Log} 注解标记的方法。 该切面拦截被 {@link Log} 注解标记的方法或使用包含 {@link Log}
 * 的元注解标记的方法。 负责处理操作日志的创建和处理，包括： 1. 记录方法执行时间 2. 捕获方法参数和返回值 3. 处理执行过程中的异常 4. 通过配置的处理器完成日志记录
 *
 * @author Yakir
 * @param <T> 操作日志实体类型
 * @since 1.0.0
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class OperationLogAspect<T> {

	/**
	 * 操作日志处理器，用于构建和处理日志记录
	 */
	private final OperationLogHandler<T> operationLogHandler;

	/**
	 * 拦截方法执行并处理操作日志。 该方法同时处理直接使用 {@link Log} 注解的方法和使用元注解的方法。
	 *
	 * 处理流程： 1. 记录方法执行开始时间 2. 提取方法上的 {@link Log} 注解信息 3. 通过处理器创建日志记录 4. 执行目标方法并捕获结果 5.
	 * 记录执行时间和异常信息 6. 通过处理器完成日志处理
	 * @param joinPoint 表示被拦截方法的连接点
	 * @return 方法执行的结果
	 * @throws Throwable 方法执行过程中的异常
	 * @see Log
	 * @see OperationLogHandler
	 */
	@Around("execution(@(@com.relaxed.common.log.operation.annotation.Log *) * *(..)) "
			+ "|| @annotation(com.relaxed.common.log.operation.annotation.Log)")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		// 记录开始时间
		long startTime = System.currentTimeMillis();
		// 获取目标方法签名
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		// 获取操作日志注解（使用 AnnotatedElementUtils 以支持元注解）
		Log operatorLog = AnnotatedElementUtils.findMergedAnnotation(method, Log.class);
		Assert.notNull(operatorLog, "operationLogging annotation must not be null!");
		// 构建日志对象
		T operationLog = operationLogHandler.buildLog(operatorLog, joinPoint);

		Throwable throwable = null;
		Object result = null;
		try {
			result = joinPoint.proceed();
			return result;
		}
		catch (Throwable e) {
			throwable = e;
			throw throwable;
		}
		finally {
			boolean isSaveResult = operatorLog.recordResult();
			handleLog(joinPoint, startTime, operationLog, throwable, isSaveResult, result);
		}
	}

	/**
	 * 处理操作日志的具体逻辑。 该方法负责计算执行时间，填充执行信息，并通过处理器保存日志。
	 * @param joinPoint 连接点，包含被拦截方法的信息
	 * @param startTime 方法执行的开始时间
	 * @param operationLog 操作日志对象
	 * @param throwable 执行过程中的异常（如果有）
	 * @param isSaveResult 是否保存执行结果
	 * @param executionResult 方法执行的返回结果
	 */
	private void handleLog(ProceedingJoinPoint joinPoint, long startTime, T operationLog, Throwable throwable,
			boolean isSaveResult, Object executionResult) {
		try {
			// 计算结束时间
			long endTime = System.currentTimeMillis();

			// 填充执行信息并处理日志
			operationLogHandler.handleLog(operationLogHandler.fillExecutionInfo(operationLog, joinPoint, startTime,
					endTime, throwable, isSaveResult, executionResult));
		}
		catch (Exception e) {
			log.error("操作日志记录失败：{}", operationLog, e);
		}
	}

}

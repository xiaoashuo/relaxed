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
 * @author Yakir
 * @Topic OperationLogAspect
 * @Description
 * @date 2021/6/27 13:01
 * @Version 1.0
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class OperationLogAspect<T> {

	private final OperationLogHandler<T> operationLogHandler;

	/**
	 * 匹配带有 Log注解的 以及任何返回值类型持有Log的公共方法
	 * @{link https://blog.csdn.net/zhengchao1991/article/details/53391244}
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("execution(@(@com.relaxed.common.log.operation.annotation.Log *) * *(..)) "
			+ "|| @annotation(com.relaxed.common.log.operation.annotation.Log)")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		// 开始时间
		long startTime = System.currentTimeMillis();
		// 获取目标方法
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		// 获取操作日志注解 备注： AnnotationUtils.findAnnotation 方法无法获取继承的属性
		Log operatorLog = AnnotatedElementUtils.findMergedAnnotation(method, Log.class);
		// 获取操作日志 DTO
		Assert.notNull(operatorLog, "operationLogging annotation must not be null!");
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

	private void handleLog(ProceedingJoinPoint joinPoint, long startTime, T operationLog, Throwable throwable,
			boolean isSaveResult, Object executionResult) {
		try {
			// 结束时间
			long endTime = System.currentTimeMillis();

			// 处理操作日志
			operationLogHandler.handleLog(operationLogHandler.fillExecutionInfo(operationLog, joinPoint, startTime,
					endTime, throwable, isSaveResult, executionResult));
		}
		catch (Exception e) {
			log.error("记录操作日志异常：{}", operationLog, e);
		}
	}

}

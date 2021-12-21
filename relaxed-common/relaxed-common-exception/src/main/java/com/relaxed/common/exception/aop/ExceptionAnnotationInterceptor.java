package com.relaxed.common.exception.aop;

import com.relaxed.common.exception.handler.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Yakir
 * @Topic ExceptionAnnotationInterceptor
 * @Description
 * @date 2021/12/21 10:06
 * @Version 1.0
 */
@RequiredArgsConstructor
@Slf4j
public class ExceptionAnnotationInterceptor implements MethodInterceptor {

	private final GlobalExceptionHandler globalExceptionHandler;

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object result;
		try {
			result = invocation.proceed();
		}
		catch (Throwable throwable) {
			globalExceptionHandler.handle(throwable);
			throw throwable;
		}
		return result;
	}

}

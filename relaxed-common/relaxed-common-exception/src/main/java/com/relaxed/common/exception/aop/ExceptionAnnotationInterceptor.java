package com.relaxed.common.exception.aop;

import cn.hutool.core.util.IdUtil;
import com.relaxed.common.exception.handler.GlobalExceptionHandler;
import com.relaxed.common.exception.holder.ExceptionHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.StringUtils;

/**
 * 异常注解拦截器
 * <p>
 * 实现了AOP的MethodInterceptor接口，用于拦截带有异常注解的方法调用。 通过全局异常处理器处理被拦截方法抛出的异常，并确保异常信息的正确传递。
 * </p>
 *
 * @author Yakir
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Slf4j
public class ExceptionAnnotationInterceptor implements MethodInterceptor {

	/**
	 * 全局异常处理器
	 * <p>
	 * 用于处理被拦截方法抛出的异常。
	 * </p>
	 */
	private final GlobalExceptionHandler globalExceptionHandler;

	/**
	 * 拦截方法调用
	 * <p>
	 * 在方法执行前后进行异常处理，确保异常信息的正确传递。 使用线程变量跟踪异常处理上下文，避免重复处理。
	 * </p>
	 * @param invocation 方法调用上下文
	 * @return 方法执行结果
	 * @throws Throwable 如果方法执行过程中抛出异常
	 */
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		// 若未开启嵌套多次通知 并以存在线程变量 则直接跳过档次调用链执行
		if (StringUtils.hasText(ExceptionHolder.getXID())) {
			return invocation.proceed();
		}
		String xid = IdUtil.simpleUUID();
		ExceptionHolder.bind(xid);
		Object result;
		try {
			result = invocation.proceed();
		}
		catch (Throwable throwable) {
			globalExceptionHandler.handle(throwable);
			throw throwable;
		}
		finally {
			ExceptionHolder.unbind(xid);
		}
		return result;
	}

}

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
 * @author Yakir
 * @Topic ExceptionAnnotationInterceptor
 * @Description
 * @date 2021/12/21 10:06
 * @Version 1.0
 */
@RequiredArgsConstructor
@Slf4j
public class ExceptionAnnotationInterceptor implements MethodInterceptor {

	private final ExceptionStrategy exceptionStrategy;

	private final GlobalExceptionHandler globalExceptionHandler;

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		// 若未开启嵌套多次通知 并以存在线程变量 则直接跳过档次调用链执行
		if (!exceptionStrategy.nestedMulNotice() && StringUtils.hasText(ExceptionHolder.getXID())) {
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

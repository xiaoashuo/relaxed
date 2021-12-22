package com.relaxed.common.exception.aop;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
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

	private final GlobalExceptionHandler globalExceptionHandler;

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
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

package com.relaxed.common.idempotent.key;

import com.relaxed.common.core.util.SpELUtil;
import com.relaxed.common.idempotent.annotation.Idempotent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

/**
 * 幂等键存储抽象类。 实现了 {@link IdempotentKeyStore} 接口，提供了幂等键构建的通用实现，主要功能包括： 1. 支持 SpEL 表达式构建幂等键
 * 2. 支持请求上下文信息获取 3. 支持自定义前缀和唯一标识
 *
 * @author Yakir
 * @since 1.0
 */
public abstract class AbstractIdempotentKeyStore implements IdempotentKeyStore {

	/**
	 * 构建幂等键。 根据幂等注解和连接点信息构建唯一的幂等键，支持： 1. 使用 SpEL 表达式提取唯一标识 2. 支持请求上下文信息 3. 支持自定义前缀
	 * @param joinPoint 连接点
	 * @param idempotentAnnotation 幂等注解
	 * @return 完整的幂等键，格式为：{prefix}:{uniqueStr}
	 */
	@Override
	public String buildIdempotentKey(ProceedingJoinPoint joinPoint, Idempotent idempotentAnnotation) {
		String uniqueExpression = idempotentAnnotation.uniqueExpression();
		// 如果没有填写表达式，直接返回 prefix
		if ("".equals(uniqueExpression)) {
			return idempotentAnnotation.prefix();
		}

		// 获取当前方法以及方法参数
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		Object[] args = joinPoint.getArgs();

		// 根据当前切点，获取到 spEL 上下文
		EvaluationContext spelContext = SpELUtil.getSpElContext(joinPoint.getTarget(), method, args);
		// 如果在 servlet 环境下，则将 request 信息放入上下文，便于获取请求参数
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		if (requestAttributes != null) {
			spelContext.setVariable(RequestAttributes.REFERENCE_REQUEST, requestAttributes.getRequest());
		}
		// 解析出唯一标识
		String uniqueStr = SpELUtil.parseValueToString(spelContext, uniqueExpression);
		// 和 prefix 拼接获得完整的 key
		return idempotentAnnotation.prefix() + ":" + uniqueStr;
	}

}

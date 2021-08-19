package com.relaxed.common.idempotent.key;

import com.relaxed.common.core.util.SpELUtil;
import com.relaxed.common.idempotent.annotation.Idempotent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

/**
 * @author Yakir
 * @Topic AbstractIdempotentKeyStore
 * @Description
 * @date 2021/8/19 13:56
 * @Version 1.0
 */
public abstract class AbstractIdempotentKeyStore implements IdempotentKeyStore {

	/**
	 * 构建幂等标识 key
	 * @param joinPoint 切点
	 * @param idempotentAnnotation 幂等注解
	 * @param method 当前方法
	 * @param args 方法参数
	 * @return String 幂等标识
	 */
	@Override
	public String buildIdempotentKey(ProceedingJoinPoint joinPoint, Idempotent idempotentAnnotation, Method method,
			Object[] args) {
		String uniqueExpression = idempotentAnnotation.uniqueExpression();
		// 如果没有填写表达式，直接返回 prefix
		if ("".equals(uniqueExpression)) {
			return idempotentAnnotation.prefix();
		}

		// 根据当前切点，获取到 spEL 上下文
		EvaluationContext spelContext = SpELUtil.getSpElContext(joinPoint.getTarget(), method, args);
		// 如果在 sevlet 环境下，则将 request 信息放入上下文，便于获取请求参数
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		if (requestAttributes != null) {
			spelContext.setVariable("request", requestAttributes.getRequest());
		}
		// 解析出唯一标识
		String uniqueStr = SpELUtil.parseValueToString(spelContext, uniqueExpression);
		// 和 prefix 拼接获得完整的 key
		return idempotentAnnotation.prefix() + ":" + uniqueStr;
	}

}

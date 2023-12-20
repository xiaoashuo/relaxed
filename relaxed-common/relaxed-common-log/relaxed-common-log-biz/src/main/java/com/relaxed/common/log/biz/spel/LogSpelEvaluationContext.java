package com.relaxed.common.log.biz.spel;

import com.relaxed.common.log.biz.constant.LogRecordConstants;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;

/**
 * @author Yakir
 * @Topic LogSpelContext
 * @Description
 * @date 2023/12/15 11:53
 * @Version 1.0
 */
public class LogSpelEvaluationContext extends MethodBasedEvaluationContext {

	private Object target;

	private Method method;

	private Object[] arguments;

	public LogSpelEvaluationContext(Method method, Object[] arguments,
			ParameterNameDiscoverer parameterNameDiscoverer) {
		this(null, method, arguments, parameterNameDiscoverer);
	}

	public LogSpelEvaluationContext(Object rootObject, Method method, Object[] arguments,
			ParameterNameDiscoverer parameterNameDiscoverer) {
		super(rootObject, method, arguments, parameterNameDiscoverer);
		this.target = rootObject;
		this.method = method;
		this.arguments = arguments;
	}

	public Object getTarget() {
		return target;
	}

	public Method getMethod() {
		return method;
	}

	public Object[] getArguments() {
		return arguments;
	}

}

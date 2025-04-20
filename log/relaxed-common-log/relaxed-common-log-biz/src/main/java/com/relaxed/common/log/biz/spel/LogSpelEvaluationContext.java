package com.relaxed.common.log.biz.spel;

import com.relaxed.common.log.biz.constant.LogRecordConstants;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;

/**
 * 日志 SpEL 求值上下文 该上下文类扩展了 Spring 的 MethodBasedEvaluationContext，用于在 SpEL 表达式中
 * 提供方法调用的上下文信息。主要功能包括： 1. 存储目标对象、方法和参数信息 2. 支持参数名称发现 3. 提供方法调用的上下文环境
 *
 * @author Yakir
 */
public class LogSpelEvaluationContext extends MethodBasedEvaluationContext {

	/**
	 * 目标对象，即方法所属的实例
	 */
	private Object target;

	/**
	 * 当前执行的方法
	 */
	private Method method;

	/**
	 * 方法参数数组
	 */
	private Object[] arguments;

	/**
	 * 构造函数 使用指定的方法、参数和参数名称发现器创建上下文
	 * @param method 方法
	 * @param arguments 参数数组
	 * @param parameterNameDiscoverer 参数名称发现器
	 */
	public LogSpelEvaluationContext(Method method, Object[] arguments,
			ParameterNameDiscoverer parameterNameDiscoverer) {
		this(null, method, arguments, parameterNameDiscoverer);
	}

	/**
	 * 构造函数 使用指定的根对象、方法、参数和参数名称发现器创建上下文
	 * @param rootObject 根对象
	 * @param method 方法
	 * @param arguments 参数数组
	 * @param parameterNameDiscoverer 参数名称发现器
	 */
	public LogSpelEvaluationContext(Object rootObject, Method method, Object[] arguments,
			ParameterNameDiscoverer parameterNameDiscoverer) {
		super(rootObject, method, arguments, parameterNameDiscoverer);
		this.target = rootObject;
		this.method = method;
		this.arguments = arguments;
	}

	/**
	 * 获取目标对象
	 * @return 目标对象
	 */
	public Object getTarget() {
		return target;
	}

	/**
	 * 获取当前执行的方法
	 * @return 方法对象
	 */
	public Method getMethod() {
		return method;
	}

	/**
	 * 获取方法参数数组
	 * @return 参数数组
	 */
	public Object[] getArguments() {
		return arguments;
	}

}

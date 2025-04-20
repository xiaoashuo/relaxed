package com.relaxed.fastexcel.processor;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;

/**
 * Spring EL表达式文件名处理器 基于Spring EL表达式实现的文件名解析器 主要功能: 1. 支持Spring EL表达式解析 2. 支持方法参数注入 3.
 * 支持动态变量解析 4. 支持表达式结果转换
 *
 * @author lengleng
 * @since 1.0.0
 */
public class NameSpelExpressionProcessor implements NameProcessor {

	/**
	 * 方法参数名称发现器 用于获取方法参数的名称
	 */
	private static final ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

	/**
	 * Spring EL表达式解析器 用于解析和执行表达式
	 */
	private static final ExpressionParser PARSER = new SpelExpressionParser();

	/**
	 * 解析文件名 使用Spring EL表达式解析文件名 处理流程: 1. 检查是否包含表达式 2. 创建表达式上下文 3. 解析表达式 4. 转换结果
	 * @param args 方法参数数组
	 * @param method 当前执行的方法
	 * @param key 文件名表达式
	 * @return 解析后的文件名,如果表达式不包含变量则返回原值
	 */
	@Override
	public String doDetermineName(Object[] args, Method method, String key) {

		if (!key.contains("#")) {
			return key;
		}

		EvaluationContext context = new MethodBasedEvaluationContext(null, method, args, NAME_DISCOVERER);
		final Object value = PARSER.parseExpression(key).getValue(context);
		return value == null ? null : value.toString();
	}

}

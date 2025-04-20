package com.relaxed.common.core.util;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Spring Expression Language (SpEL) 工具类，提供便捷的表达式解析和求值功能。
 *
 * 主要功能： 1. SpEL 表达式解析和求值 2. 支持方法参数解析和上下文创建 3. 提供多种类型的表达式求值结果（String、Boolean、List等） 4.
 * 支持自定义解析上下文
 *
 * 使用场景： - 动态属性访问 - 方法调用 - 条件表达式求值 - 集合操作
 *
 * 示例： <pre>
 * // 简单算术表达式
 * String result = parseValueToString(context, "3 + 4");
 *
 * // 属性访问
 * String name = parseValueToString(context, "person.name");
 *
 * // 方法调用
 * String substring = parseValueToString(context, "str.substring(2, 5)");
 * </pre>
 *
 * @author Yakir
 * @since 1.0
 */
public class SpELUtil {

	/**
	 * SpEL 表达式解析器 用于解析和执行 SpEL 表达式
	 */
	public static final SpelExpressionParser PARSER = new SpelExpressionParser();

	/**
	 * 方法参数名称发现器 用于获取方法的参数名称，支持调试信息或注解方式
	 */
	private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

	/**
	 * 获取指定 SpEL 表达式的解析结果
	 * @param expression SpEL 表达式字符串
	 * @return 解析后的表达式对象，可用于重复求值
	 * @throws ParseException 表达式解析异常
	 */
	public static Expression getExpression(String expression) {
		return getParser().parseExpression(expression);
	}

	/**
	 * 使用指定的解析上下文解析 SpEL 表达式
	 *
	 * 示例： <pre>
	 * 1. 简单算术：'3 + 4'
	 * 2. 属性访问：'person.name'
	 * 3. 方法调用：'str.substring(2, 5)'
	 * </pre>
	 * @param expression SpEL 表达式字符串
	 * @param parserContext 解析上下文，影响表达式的解析行为
	 * @return 解析后的表达式对象
	 * @throws ParseException 表达式解析异常
	 */
	public static Expression getExpression(String expression, ParserContext parserContext) {
		return getParser().parseExpression(expression, parserContext);
	}

	/**
	 * 将 SpEL 表达式解析结果转换为字符串
	 * @param context 表达式执行上下文
	 * @param spEL SpEL 表达式
	 * @return 解析后的字符串值
	 */
	public static String parseValueToString(EvaluationContext context, String spEL) {
		return getParser().parseExpression(spEL).getValue(context, String.class);
	}

	/**
	 * 将 SpEL 表达式解析结果转换为布尔值
	 * @param context 表达式执行上下文
	 * @param spEL SpEL 表达式
	 * @return 解析后的布尔值，如果解析结果为 null 则返回 false
	 */
	public static boolean parseValueToBool(EvaluationContext context, String spEL) {
		return Boolean.TRUE.equals(getParser().parseExpression(spEL).getValue(context, Boolean.class));
	}

	/**
	 * 将 SpEL 表达式解析结果转换为字符串列表
	 * @param context 表达式执行上下文
	 * @param spelExpression SpEL 表达式，表达式结果应该是一个集合或数组
	 * @return 解析后的字符串列表，如果解析失败则可能返回 null
	 */
	public static List<String> parseValueToStringList(EvaluationContext context, String spelExpression) {
		return getParser().parseExpression(spelExpression).getValue(context, List.class);
	}

	/**
	 * 创建方法执行的 SpEL 上下文，支持通过索引访问参数（如 #p0、#p1）
	 * @param rootObject 根对象，通常是方法所在的类实例
	 * @param method 目标方法
	 * @param args 方法参数值数组
	 * @return 包含方法执行环境的 SpEL 上下文
	 */
	public static StandardEvaluationContext getSpElContextByManual(Object rootObject, Method method, Object[] args) {
		String[] paraNameArr = PARAMETER_NAME_DISCOVERER.getParameterNames(method);
		// SPEL 上下文
		StandardEvaluationContext context = new MethodBasedEvaluationContext(rootObject, method, args,
				PARAMETER_NAME_DISCOVERER);
		// 把方法参数放入 SPEL 上下文中
		for (int i = 0; i < paraNameArr.length; i++) {
			context.setVariable(paraNameArr[i], args[i]);
		}
		return context;
	}

	/**
	 * 创建标准的 SpEL 执行上下文
	 * @param target 目标对象，作为根对象
	 * @param method 要执行的方法
	 * @param args 方法参数值数组
	 * @return 标准的 SpEL 执行上下文
	 */
	public static StandardEvaluationContext getSpElContext(Object target, Method method, Object[] args) {
		return new MethodBasedEvaluationContext(target, method, args, getParameterNameDiscoverer());
	}

	/**
	 * 获取 SpEL 表达式解析器实例
	 * @return SpEL 表达式解析器
	 */
	public static SpelExpressionParser getParser() {
		return PARSER;
	}

	/**
	 * 获取参数名称发现器实例
	 * @return 参数名称发现器
	 */
	private static ParameterNameDiscoverer getParameterNameDiscoverer() {
		return PARAMETER_NAME_DISCOVERER;
	}

}

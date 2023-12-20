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
 * @author Yakir
 * @Topic SpELUtil
 * @Description
 * @date 2021/7/24 13:01
 * @Version 1.0
 */
public class SpELUtil {

	/**
	 * SpEL 解析器
	 */
	public static final SpelExpressionParser PARSER = new SpelExpressionParser();

	/**
	 * 方法参数获取
	 */
	private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

	/**
	 * Return the {@link Expression} for the specified SpEL value
	 * <p>
	 * Parse the expression if it hasn't been already.
	 * @param expression the expression to parse
	 */
	public static Expression getExpression(String expression) {
		return getParser().parseExpression(expression);
	}

	/**
	 * Parse the expression string and return an Expression object you can use for
	 * repeated evaluation.
	 * <p>
	 * Some examples: <pre class="code">
	 *     3 + 4
	 *     name.firstName
	 * </pre>
	 * @param expression the raw expression string to parse
	 * @param parserContext a context for influencing this expression parsing routine
	 * (optional)
	 * @return an evaluator for the parsed expression
	 * @throws ParseException an exception occurred during parsing
	 */
	public static Expression getExpression(String expression, ParserContext parserContext) {
		return getParser().parseExpression(expression, parserContext);
	}

	/**
	 * 格式化值SpEL表达式
	 * @param context
	 * @param spEL
	 * @return
	 */
	public static String parseValueToString(EvaluationContext context, String spEL) {
		return getParser().parseExpression(spEL).getValue(context, String.class);
	}

	/**
	 * 格式化值 到Boolean类型
	 * @param context
	 * @param spEL
	 * @return
	 */
	public static boolean parseValueToBool(EvaluationContext context, String spEL) {
		return Boolean.TRUE.equals(getParser().parseExpression(spEL).getValue(context, Boolean.class));
	}

	/**
	 * 解析 spel 表达式
	 * @param context spel 上下文
	 * @param spelExpression spel 表达式
	 * @return 解析后的 List<String>
	 */
	public static List<String> parseValueToStringList(EvaluationContext context, String spelExpression) {
		return getParser().parseExpression(spelExpression).getValue(context, List.class);
	}

	/**
	 * 支持 #p0 参数索引的表达式解析 手动填充参数
	 * @param rootObject 根对象,method 所在的对象
	 * @param method ，目标方法
	 * @param args 方法入参
	 * @return 解析后的字符串
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
	 * 创建执行上下文
	 * @param target
	 * @param method
	 * @param args
	 * @return
	 */
	public static StandardEvaluationContext getSpElContext(Object target, Method method, Object[] args) {
		StandardEvaluationContext standardEvaluationContext = new MethodBasedEvaluationContext(target, method, args,
				getParameterNameDiscoverer());

		return standardEvaluationContext;
	}

	/**
	 * Return the {@link SpelExpressionParser} to use.
	 */
	public static SpelExpressionParser getParser() {
		return PARSER;
	}

	private static ParameterNameDiscoverer getParameterNameDiscoverer() {
		return PARAMETER_NAME_DISCOVERER;
	}

}

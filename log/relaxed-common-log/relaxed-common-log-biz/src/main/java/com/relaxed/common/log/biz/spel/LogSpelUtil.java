package com.relaxed.common.log.biz.spel;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.relaxed.common.log.biz.context.LogRecordContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * SpEL 表达式工具类 该工具类提供了 SpEL 表达式解析和求值的相关功能 主要功能包括： 1. 构建 SpEL 上下文环境 2. 注册全局函数和变量 3.
 * 解析各种类型的表达式 4. 支持参数名称发现和参数值转换
 *
 * @author Yakir
 */
public class LogSpelUtil {

	/**
	 * 方法参数名称发现器 用于获取方法参数的名称
	 */
	private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

	/**
	 * SpEL 表达式解析器 用于解析和求值 SpEL 表达式
	 */
	private static final SpelExpressionParser parser = new SpelExpressionParser();

	/**
	 * 构建 SpEL 上下文 创建并初始化 SpEL 上下文环境，包括： 1. 设置目标对象、方法和参数 2. 注册全局变量 3. 注册全局函数
	 * @param target 目标对象
	 * @param method 方法
	 * @param args 参数
	 * @return SpEL 上下文
	 */
	public static LogSpelEvaluationContext buildSpelContext(Object target, Method method, Object[] args) {
		LogSpelEvaluationContext spElContext = new LogSpelEvaluationContext(target, method, args,
				PARAMETER_NAME_DISCOVERER);
		// 注册当前方法全局变量参数
		registerGlobalParam(spElContext);
		// 注册全局函数
		registerGlobalFuncs(spElContext);
		return spElContext;
	}

	/**
	 * 注册全局 SpEL 方法 支持两种方式： 1. 使用方法解析器（当前实现） 2. 注册函数（注释掉的实现）
	 * @param spElContext SpEL 上下文
	 */
	private static void registerGlobalFuncs(StandardEvaluationContext spElContext) {
		// 采用方法解析器形式
		spElContext.addMethodResolver(new LogMethodResolver());
		// 采用注册函数形式
		// Map<String, FuncMeta> functionMap = LogRecordFuncDiscover.getFunctionMap();
		// functionMap.forEach((methodName,funcMeta)->
		// spElContext.registerFunction(methodName,funcMeta.getMethod()));
	}

	/**
	 * 注册全局变量参数 将 LogRecordContext 中的变量注册到 SpEL 上下文中
	 * @param logRecordContext SpEL 上下文
	 */
	public static void registerGlobalParam(StandardEvaluationContext logRecordContext) {
		// 把 LogRecordContext 中的变量都放到 RootObject 中
		Map<String, Object> variables = LogRecordContext.peek();
		if (variables != null && variables.size() > 0) {
			for (Map.Entry<String, Object> entry : variables.entrySet()) {
				logRecordContext.setVariable(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * 解析表达式为布尔值
	 * @param spel SpEL 表达式
	 * @param context SpEL 上下文
	 * @return 布尔值结果
	 */
	public static boolean parseParamToBoolean(String spel, StandardEvaluationContext context) {
		Expression conditionExpression = parser.parseExpression(spel);
		return Boolean.TRUE.equals(conditionExpression.getValue(context, Boolean.class));
	}

	/**
	 * 解析表达式为对象
	 * @param spel SpEL 表达式
	 * @param context SpEL 上下文
	 * @return 表达式求值结果
	 */
	public static Object parseExpression(String spel, StandardEvaluationContext context) {
		Expression conditionExpression = parser.parseExpression(spel);
		return conditionExpression.getValue(context);
	}

	/**
	 * 解析表达式为字符串
	 * @param spel SpEL 表达式
	 * @param context SpEL 上下文
	 * @return 字符串结果
	 */
	public static String parseParamToString(String spel, StandardEvaluationContext context) {
		Expression bizIdExpression = parser.parseExpression(spel);
		return bizIdExpression.getValue(context, String.class);
	}

	/**
	 * 解析表达式为字符串或 JSON 如果结果不是字符串类型，则转换为 JSON 字符串
	 * @param spel SpEL 表达式
	 * @param context SpEL 上下文
	 * @return 字符串或 JSON 字符串
	 */
	public static String parseParamToStringOrJson(String spel, StandardEvaluationContext context) {
		Expression msgExpression = parser.parseExpression(spel);
		Object obj = msgExpression.getValue(context, Object.class);
		if (obj != null) {
			return obj instanceof String ? (String) obj : JSONUtil.toJsonStr(obj);
		}
		return null;
	}

	/**
	 * 将参数字符串转换为参数值数组 支持逗号分隔的多个参数表达式
	 * @param logRecordContext SpEL 上下文
	 * @param paramNames 参数名字符串，逗号分隔
	 * @return 参数值数组
	 */
	public static Object[] parseParamStrToValArr(LogSpelEvaluationContext logRecordContext, String paramNames) {
		Object[] funcArgs = null;
		if (StrUtil.isNotBlank(paramNames)) {
			String[] paramNameExps = paramNames.split(StrUtil.COMMA);
			funcArgs = new Object[paramNameExps.length];
			for (int i = 0; i < paramNameExps.length; i++) {
				funcArgs[i] = LogSpelUtil.parseExpression(paramNameExps[i], logRecordContext);
			}
		}
		return funcArgs;
	}

}

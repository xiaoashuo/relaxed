package com.relaxed.common.log.biz.spel;

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
 * @author Yakir
 * @Topic LogSeplUtil
 * @Description
 * @date 2023/12/19 14:39
 * @Version 1.0
 */
public class LogSeplUtil {

	/**
	 * 方法参数获取
	 */
	private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

	private static final SpelExpressionParser parser = new SpelExpressionParser();

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
	 * 注册全局的spel方法
	 * @param spElContext
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
	 * 注册全局变量参数
	 * @param logRecordContext
	 */
	public static void registerGlobalParam(StandardEvaluationContext logRecordContext) {
		// 把 LogRecordContext 中的变量都放到 RootObject 中
		Map<String, Object> variables = LogRecordContext.peek();
		if (variables != null && variables.size() > 0) {
			for (Map.Entry<String, Object> entry : variables.entrySet()) {
				logRecordContext.setVariable(entry.getKey(), entry.getValue());
			}
		}
		// if (beanFactory != null) {
		// // setBeanResolver 主要用于支持SpEL模板中调用指定类的方法，如：@XXService.x(#root)
		// //如果获得工厂方法自己，bean名字须要添加&前缀而不是@
		// logRecordContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
		// }
	}

	public static boolean parseParamToBoolean(String spel, StandardEvaluationContext context) {
		Expression conditionExpression = parser.parseExpression(spel);
		return Boolean.TRUE.equals(conditionExpression.getValue(context, Boolean.class));
	}

	public static Object parseExpression(String spel, StandardEvaluationContext context) {
		Expression conditionExpression = parser.parseExpression(spel);
		return conditionExpression.getValue(context);
	}

	public static String parseParamToString(String spel, StandardEvaluationContext context) {
		Expression bizIdExpression = parser.parseExpression(spel);
		return bizIdExpression.getValue(context, String.class);
	}

	public static String parseParamToStringOrJson(String spel, StandardEvaluationContext context) {
		Expression msgExpression = parser.parseExpression(spel);
		Object obj = msgExpression.getValue(context, Object.class);
		if (obj != null) {
			return obj instanceof String ? (String) obj : JSONUtil.toJsonStr(obj);
		}
		return null;
	}

}

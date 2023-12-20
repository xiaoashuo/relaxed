package com.relaxed.common.log.test.operator.spel;

import com.relaxed.common.log.operation.discover.func.FuncMeta;
import com.relaxed.common.log.operation.discover.func.LogRecordFuncDiscover;
import com.relaxed.common.log.operation.spel.LogMethodResolver;
import com.relaxed.common.log.test.operator.User;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * @author Yakir
 * @Topic SpelTest
 * @Description
 * @date 2023/12/18 14:46
 * @Version 1.0
 */
public class SpelTest {

	public static String myMethod(String arg) {
		return "modified" + arg;
	}

	public String nonStaticMethod(String arg, String arg2) {
		return "modified" + arg + "-" + arg2;
	}

	public static void main(String[] args) throws Exception {
		Method myMethod = SpelTest.class.getMethod("myMethod", String.class);
		Method nonStaticMethod = SpelTest.class.getMethod("nonStaticMethod", String.class, String.class);
		FuncMeta staticFuncMeta = new FuncMeta("myMethod", true, SpelTest.class, myMethod,"");
		FuncMeta nonStaticFuncMeta = new FuncMeta("nonStaticMethod", false, new SpelTest(), nonStaticMethod,"");

		LogRecordFuncDiscover.regFunc("myMethod", staticFuncMeta);
		LogRecordFuncDiscover.regFunc("nonStaticMethod", nonStaticFuncMeta);
		// 创建 SpEL 解析器
		SpelExpressionParser parser = new SpelExpressionParser();

		// 创建 EvaluationContext
		StandardEvaluationContext context = new StandardEvaluationContext();
		User rootObject = new User();
		rootObject.setUsername("扎昂三");
		context.setRootObject(rootObject);
		context.addMethodResolver(new LogMethodResolver());
		// // 定义一个包含方法的表达式
		// String propertyExpression = "#root.username";
		//
		// // 解析并计算表达式的值
		// String propertyResult =
		// parser.parseExpression(propertyExpression).getValue(context, String.class);
		//
		// // 打印结果
		// System.out.println("Property Result: " + propertyResult);
		// 定义一个包含方法的表达式
		String expression = "'测试'+myMethod('Hello')+'测试2'+myNullMethod('Hello')";

		// 解析并计算表达式的值
		String result = parser.parseExpression(expression).getValue(context, String.class);

		// 打印结果
		System.out.println("Result: " + result);
		// // 定义一个包含方法的表达式
		// String nonStaticMethodExpression = "nonStaticMethod('Hello','joke')";
		//
		// // 解析并计算表达式的值
		// String nonStaticMethodResult =
		// parser.parseExpression(nonStaticMethodExpression).getValue(context,
		// String.class);
		//
		// // 打印结果
		// System.out.println("nonStaticMethod Result: " + nonStaticMethodResult);
	}

}

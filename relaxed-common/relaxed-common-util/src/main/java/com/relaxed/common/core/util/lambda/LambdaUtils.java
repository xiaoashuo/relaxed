package com.relaxed.common.core.util.lambda;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.relaxed.common.core.util.lambda.SerializedLambda;
import lombok.Data;

import javax.management.ReflectionException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Locale;

/**
 * Lambda表达式工具类。 提供了对Lambda表达式的元数据提取功能，支持以下场景： 1. IDEA代理的Lambda表达式 2. 标准JDK的Lambda表达式 3.
 * 特殊场景下的Lambda表达式
 *
 * 该工具类主要用于获取Lambda表达式中的方法引用信息， 可用于构建动态查询条件、属性映射等场景。
 *
 * @author Yakir
 * @since 1.0
 */
public class LambdaUtils {

	/**
	 * 私有构造函数
	 */
	public LambdaUtils() {
	}

	/**
	 * 提取Lambda表达式的元数据信息
	 * @param <T> Lambda表达式的目标类型
	 * @param func Lambda表达式，通常是方法引用（例如 Entity::getId）
	 * @return Lambda元数据，包含方法名、字段名等信息
	 */
	public static <T> LambdaMeta extract(SFunction<T, ?> func) {
		if (func instanceof Proxy) {
			// IDEA环境下的Lambda表达式
			return new IdeaProxyLambdaMeta((Proxy) func);
		}
		else {
			try {
				// 标准JDK的Lambda表达式
				Method method = func.getClass().getDeclaredMethod("writeReplace");
				return new ReflectLambdaMeta(
						(SerializedLambda) ((Method) ReflectUtil.setAccessible(method)).invoke(func));
			}
			catch (Throwable var2) {
				// 特殊场景下的Lambda表达式
				return new ShadowLambdaMeta(SerializedLambda.extract(func));
			}
		}
	}

}

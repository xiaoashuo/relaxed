package com.relaxed.oauth2.auth.util;

import cn.hutool.core.util.StrUtil;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 类工具类 提供类相关的工具方法，主要用于反射操作 包含方法缓存机制，提高反射性能
 *
 * @author Yakir
 * @since 1.0
 */
public class ClassUtil {

	/**
	 * 方法缓存 key: 类名-方法名-参数类型列表 value: 对应的方法对象
	 */
	private static Map<String, Method> CLASS_METHOD_CACHE = new HashMap<>(16);

	/**
	 * 获取类中指定名称和参数类型的方法 支持方法缓存，避免重复反射
	 * @param clazz 目标类
	 * @param methodName 方法名称
	 * @param args 方法参数，用于确定参数类型
	 * @return 匹配的方法对象
	 * @throws RuntimeException 当找不到匹配的方法时抛出
	 */
	public static Method getMethod(Class clazz, String methodName, Object... args) {
		Class<?>[] paramTypes = cn.hutool.core.util.ClassUtil.getClasses(args);
		String argTypeStr = Arrays.stream(paramTypes).map(Class::getSimpleName).collect(Collectors.joining("-"));
		String key = clazz.getName() + "-" + methodName + "-" + argTypeStr;
		Method cacheMethod = CLASS_METHOD_CACHE.get(key);
		if (cacheMethod != null) {
			return cacheMethod;
		}

		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			if (StrUtil.equals(methodName, method.getName(), false)
					&& cn.hutool.core.util.ClassUtil.isAllAssignableFrom(method.getParameterTypes(), paramTypes)
					&& !method.isBridge()) {
				CLASS_METHOD_CACHE.put(key, method);
				return method;
			}
		}
		throw new RuntimeException("方法:" + key + "not found");
	}

}

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
 * @author Yakir
 * @Topic ClassUtil
 * @Description
 * @date 2022/7/29 16:03
 * @Version 1.0
 */
public class ClassUtil {

	private static Map<String, Method> CLASS_METHOD_CACHE = new HashMap<>(16);

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

package com.relaxed.common.datascope.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 注解工具类
 * <p>
 * 提供注解相关的工具方法，主要用于查找类和方法上的注解。
 */
public final class AnnotationUtil {

	private AnnotationUtil() {
	}

	/**
	 * 查找注解
	 * <p>
	 * 根据 Mapper 方法 ID 查找指定的注解，优先查找方法上的注解，如果未找到则查找类上的注解。
	 * @param mappedStatementId Mapper 方法 ID，格式为"类名.方法名"
	 * @param aClass 要查找的注解类型
	 * @param <A> 注解类型
	 * @return 找到的注解，如果未找到则返回 null
	 */
	public static <A extends Annotation> A findAnnotationByMappedStatementId(String mappedStatementId,
			Class<A> aClass) {
		if (mappedStatementId == null || "".equals(mappedStatementId)) {
			return null;
		}
		// 1.得到类路径和方法路径
		int lastIndexOfDot = mappedStatementId.lastIndexOf(".");
		if (lastIndexOfDot < 0) {
			return null;
		}
		String className = mappedStatementId.substring(0, lastIndexOfDot);
		String methodName = mappedStatementId.substring(lastIndexOfDot + 1);
		if ("".equals(className) || "".equals(methodName)) {
			return null;
		}

		// 2.字节码
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (clazz == null) {
			return null;
		}

		A annotation = null;
		// 3.得到方法上的注解
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			String name = method.getName();
			if (methodName.equals(name)) {
				annotation = method.getAnnotation(aClass);
				break;
			}
		}
		if (annotation == null) {
			annotation = clazz.getAnnotation(aClass);
		}
		return annotation;
	}

}

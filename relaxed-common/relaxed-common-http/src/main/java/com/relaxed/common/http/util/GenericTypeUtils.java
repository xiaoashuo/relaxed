package com.relaxed.common.http.util;

/**
 * @author Yakir
 * @Topic GenericTypeUtils
 * @Description
 * @date 2022/4/24 16:30
 * @Version 1.0
 */
public class GenericTypeUtils {

	private static IGenericTypeResolver GENERIC_TYPE_RESOLVER;

	/**
	 * 获取泛型工具助手
	 */
	public static Class<?>[] resolveTypeArguments(final Class<?> clazz, final Class<?> genericIfc) {
		if (null == GENERIC_TYPE_RESOLVER) {
			// 直接使用 spring 静态方法，减少对象创建
			return SpringReflectionHelper.resolveTypeArguments(clazz, genericIfc);
		}
		return GENERIC_TYPE_RESOLVER.resolveTypeArguments(clazz, genericIfc);
	}

	/**
	 * 设置泛型工具助手。如果不想使用Spring封装，可以使用前替换掉
	 */
	public static void setGenericTypeResolver(IGenericTypeResolver genericTypeResolver) {
		GENERIC_TYPE_RESOLVER = genericTypeResolver;
	}

}

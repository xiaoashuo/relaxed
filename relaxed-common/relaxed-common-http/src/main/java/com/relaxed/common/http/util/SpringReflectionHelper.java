package com.relaxed.common.http.util;

import org.springframework.core.GenericTypeResolver;

/**
 * @author Yakir
 * @Topic SpringReflectionHelper
 * @Description
 * @date 2022/4/24 16:30
 * @Version 1.0
 */
public class SpringReflectionHelper {

	public static Class<?>[] resolveTypeArguments(Class<?> clazz, Class<?> genericIfc) {
		return GenericTypeResolver.resolveTypeArguments(clazz, genericIfc);
	}

}

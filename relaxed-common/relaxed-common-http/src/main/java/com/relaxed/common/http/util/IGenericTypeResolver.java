package com.relaxed.common.http.util;

/**
 * @author Yakir
 * @Topic IGenericTypeResolver
 * @Description
 * @date 2022/4/24 16:30
 * @Version 1.0
 */
public interface IGenericTypeResolver {

	Class<?>[] resolveTypeArguments(final Class<?> clazz, final Class<?> genericIfc);

}

package com.relaxed.common.http.util;

/**
 * @author Yakir
 * @Topic ReflectionUtil
 * @Description
 * @date 2022/4/24 16:31
 * @Version 1.0
 */
public class ReflectionUtil {

	/**
	 * <p>
	 * 反射对象获取泛型
	 * </p>
	 * @param clazz 对象
	 * @param genericIfc 所属泛型父类
	 * @param index 泛型所在位置
	 * @return Class
	 */
	public static Class<?> getSuperClassGenericType(final Class<?> clazz, final Class<?> genericIfc, final int index) {
		// update by noear @2021-09-03
		Class<?>[] typeArguments = GenericTypeUtils.resolveTypeArguments(ClassUtil.getUserClass(clazz), genericIfc);
		return null == typeArguments ? null : typeArguments[index];
	}

}

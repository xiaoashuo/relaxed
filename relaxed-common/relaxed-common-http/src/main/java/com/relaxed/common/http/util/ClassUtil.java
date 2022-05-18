package com.relaxed.common.http.util;

import cn.hutool.core.util.TypeUtil;
import org.springframework.util.Assert;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yakir
 * @Topic ClassUtil
 * @Description
 * @date 2022/4/24 16:29
 * @Version 1.0
 */
public class ClassUtil {

	/**
	 * 代理 class 的名称
	 */
	private static final List<String> PROXY_CLASS_NAMES = Arrays.asList("net.sf.cglib.proxy.Factory"
	// cglib
			, "org.springframework.cglib.proxy.Factory", "javassist.util.proxy.ProxyObject"
			// javassist
			, "org.apache.ibatis.javassist.util.proxy.ProxyObject");

	/**
	 * <p>
	 * 获取当前对象的 class
	 * </p>
	 * @param clazz 传入
	 * @return 如果是代理的class，返回父 class，否则返回自身
	 */
	public static Class<?> getUserClass(Class<?> clazz) {
		Assert.notNull(clazz, "Class must not be null");
		return isProxy(clazz) ? clazz.getSuperclass() : clazz;
	}

	/**
	 * 判断是否为代理对象
	 * @param clazz 传入 class 对象
	 * @return 如果对象class是代理 class，返回 true
	 */
	public static boolean isProxy(Class<?> clazz) {
		if (clazz != null) {
			for (Class<?> cls : clazz.getInterfaces()) {
				if (PROXY_CLASS_NAMES.contains(cls.getName())) {
					return true;
				}
			}
		}
		return false;
	}

	private static Map<String, Class<?>> classGenricTypeCache = new HashMap<>(16);

	public static Class<?> currentResponseClass(Class<?> clazz, Integer index) {
		String cacheKey = clazz.getName() + index;
		Class<?> cacheClass = classGenricTypeCache.get(cacheKey);
		if (cacheClass != null) {
			return cacheClass;
		}
		Type typeArgument = TypeUtil.getTypeArgument(clazz, index);
		if (typeArgument == null) {
			throw new RuntimeException("泛型参数不正确");
		}
		if (!(typeArgument instanceof Class)) {
			throw new RuntimeException("参数不是class类型");
		}
		Class<?> retClass = (Class<?>) typeArgument;
		Assert.isTrue(!retClass.isInterface(), "the response must be a class, not an interface");
		classGenricTypeCache.put(cacheKey, retClass);
		return retClass;
	}

}

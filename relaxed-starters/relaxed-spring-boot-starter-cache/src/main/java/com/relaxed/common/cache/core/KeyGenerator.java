package com.relaxed.common.cache.core;

import java.lang.reflect.Method;

/**
 * @author Yakir
 * @Topic KeyGenerator
 * @Description
 * @date 2021/7/23 17:38
 * @Version 1.0
 */
public interface KeyGenerator {

	/**
	 * key 生成器
	 * @param target 目标对象
	 * @param method 方法
	 * @param params 参数
	 * @return
	 */
	String generate(Object target, Method method, Object... params);

}

package com.relaxed.oauth2.auth.util;

import lombok.RequiredArgsConstructor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author Yakir
 * @Topic ProxyFactory
 * @Description
 * @date 2022/7/28 15:20
 * @Version 1.0
 */
public class ProxyFactory {

	/**
	 * 创建代理
	 * @param clazz
	 * @param methodInterceptor
	 * @param <T>
	 * @return
	 */
	public static <T> T create(Class<T> clazz, MethodInterceptor methodInterceptor) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(clazz);
		enhancer.setCallback(methodInterceptor);
		return (T) enhancer.create();
	}

	/**
	 * 根据实例创建
	 * @author yakir
	 * @date 2022/7/28 21:18
	 * @param obj
	 * @param methodInterceptor
	 * @return T
	 */
	public static <T> T create(T obj, MethodInterceptor methodInterceptor) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(obj.getClass());
		enhancer.setCallback(methodInterceptor);
		return (T) enhancer.create();
	}

}

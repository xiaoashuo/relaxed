package com.relaxed.oauth2.auth.util;

import com.relaxed.common.core.util.SpringUtils;
import com.relaxed.oauth2.auth.handler.AuthorizationInfoHandle;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.Assert;

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
	 * @author yakir
	 * @date 2022/7/29 14:07
	 * @param clazz
	 * @return T
	 */
	public static <T> T create(Class<T> clazz) {
		AuthorizationInfoHandle authorizationInfoHandle = SpringUtils.getBean(AuthorizationInfoHandle.class);
		Assert.notNull(authorizationInfoHandle, "授权信息处理器不能为空");
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(clazz);
		enhancer.setCallback(new PreMethodInterceptor(authorizationInfoHandle));
		return (T) enhancer.create();
	}

	/**
	 * 创建代理
	 * @author yakir
	 * @date 2022/7/29 14:07
	 * @param obj
	 * @return T
	 */
	public static <T> T create(T obj) {
		AuthorizationInfoHandle authorizationInfoHandle = SpringUtils.getBean(AuthorizationInfoHandle.class);
		Assert.notNull(authorizationInfoHandle, "授权信息处理器不能为空");
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(obj.getClass());
		enhancer.setCallback(new PreMethodInterceptor(authorizationInfoHandle));
		return (T) enhancer.create();
	}

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
	 *
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

package com.relaxed.oauth2.auth.util;

import com.relaxed.common.core.util.SpringContextUtil;
import com.relaxed.oauth2.auth.handler.AuthorizationInfoHandle;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.util.Assert;

/**
 * 代理工厂类 用于创建基于CGLIB的动态代理对象 支持根据类或实例创建代理，并可自定义方法拦截器
 *
 * @author Yakir
 * @since 1.0
 */
public class ProxyFactory {

	/**
	 * 根据类创建代理对象 使用默认的PreMethodInterceptor作为方法拦截器
	 * @param clazz 要创建代理的类
	 * @param <T> 代理对象的类型
	 * @return 代理对象
	 * @throws IllegalArgumentException 当授权信息处理器为空时抛出
	 */
	public static <T> T create(Class<T> clazz) {
		AuthorizationInfoHandle authorizationInfoHandle = SpringContextUtil.getBean(AuthorizationInfoHandle.class);
		Assert.notNull(authorizationInfoHandle, "授权信息处理器不能为空");
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(clazz);
		enhancer.setCallback(new PreMethodInterceptor(authorizationInfoHandle));
		return (T) enhancer.create();
	}

	/**
	 * 根据实例创建代理对象 使用默认的PreMethodInterceptor作为方法拦截器
	 * @param obj 要创建代理的实例
	 * @param <T> 代理对象的类型
	 * @return 代理对象
	 * @throws IllegalArgumentException 当授权信息处理器为空时抛出
	 */
	public static <T> T create(T obj) {
		AuthorizationInfoHandle authorizationInfoHandle = SpringContextUtil.getBean(AuthorizationInfoHandle.class);
		Assert.notNull(authorizationInfoHandle, "授权信息处理器不能为空");
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(obj.getClass());
		enhancer.setCallback(new PreMethodInterceptor(authorizationInfoHandle));
		return (T) enhancer.create();
	}

	/**
	 * 根据类和自定义方法拦截器创建代理对象
	 * @param clazz 要创建代理的类
	 * @param methodInterceptor 自定义的方法拦截器
	 * @param <T> 代理对象的类型
	 * @return 代理对象
	 */
	public static <T> T create(Class<T> clazz, MethodInterceptor methodInterceptor) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(clazz);
		enhancer.setCallback(methodInterceptor);
		return (T) enhancer.create();
	}

	/**
	 * 根据实例和自定义方法拦截器创建代理对象
	 * @param obj 要创建代理的实例
	 * @param methodInterceptor 自定义的方法拦截器
	 * @param <T> 代理对象的类型
	 * @return 代理对象
	 */
	public static <T> T create(T obj, MethodInterceptor methodInterceptor) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(obj.getClass());
		enhancer.setCallback(methodInterceptor);
		return (T) enhancer.create();
	}

}

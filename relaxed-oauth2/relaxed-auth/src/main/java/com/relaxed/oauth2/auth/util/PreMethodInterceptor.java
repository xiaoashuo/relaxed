package com.relaxed.oauth2.auth.util;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.relaxed.oauth2.auth.handler.AuthorizationInfoHandle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.lang.reflect.Method;

/**
 * 方法前置拦截器 用于在方法执行前根据客户端ID选择对应的用户详情服务 支持动态切换不同客户端的用户详情服务实现
 *
 * @author Yakir
 * @since 1.0
 */
@RequiredArgsConstructor
@Slf4j
public class PreMethodInterceptor implements MethodInterceptor {

	/**
	 * 授权信息处理器 用于管理客户端和用户详情服务的映射关系
	 */
	private final AuthorizationInfoHandle authorizationInfoHandle;

	/**
	 * 拦截方法调用 根据客户端ID选择对应的用户详情服务执行方法
	 * @param o 被代理的对象
	 * @param method 被调用的方法
	 * @param args 方法参数
	 * @param methodProxy 方法代理
	 * @return 方法执行结果
	 * @throws Throwable 当方法执行过程中发生异常时抛出
	 */
	@Override
	public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		// 获取客户端id
		String oAuth2ClientId = RequestUtil.getOAuth2ClientId();
		// 若不存在客户端id 则直接调用原始方法
		if (StrUtil.isEmpty(oAuth2ClientId)) {
			return methodProxy.invokeSuper(o, args);
		}
		// 1.获取当前方法名称
		String methodName = method.getName();
		// 2.获取当前客户端id 对应的service
		UserDetailsService customService = authorizationInfoHandle.obtainClient(oAuth2ClientId);
		// 3.执行当前对应service的对应方法
		Object result;
		if (customService != null) {
			try {
				Method customMethod = ClassUtil.getMethod(customService.getClass(), methodName, args);
				result = customMethod.invoke(customService, args);
			}
			catch (Exception e) {
				throw ExceptionUtil.unwrap(e);
			}
		}
		else {
			result = methodProxy.invokeSuper(o, args);
		}
		return result;
	}

}

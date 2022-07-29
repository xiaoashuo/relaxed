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
 * @author Yakir
 * @Topic PreMethodInterceptor
 * @Description 选择客户端 与对应的逻辑处理器
 * @date 2022/7/28 15:22
 * @Version 1.0
 */
@RequiredArgsConstructor
@Slf4j
public class PreMethodInterceptor implements MethodInterceptor {

	private final AuthorizationInfoHandle authorizationInfoHandle;

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

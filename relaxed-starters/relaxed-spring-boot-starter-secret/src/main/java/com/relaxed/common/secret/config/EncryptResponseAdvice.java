package com.relaxed.common.secret.config;

import com.relaxed.common.secret.annotation.RequestDecrypt;
import com.relaxed.common.secret.annotation.ResponseEncrypt;
import com.relaxed.common.secret.handler.SecretHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;

/**
 * 响应加密处理器 用于处理带有@ResponseEncrypt注解的响应，支持响应体加密 实现了ResponseBodyAdvice接口，在响应体写入前进行加密处理
 *
 * @author Yakir
 * @since 1.0
 */
@RequiredArgsConstructor
@RestControllerAdvice
public class EncryptResponseAdvice implements ResponseBodyAdvice<Object> {

	private final SecretHandler secretHandler;

	/**
	 * 判断是否支持对当前响应进行加密处理 检查方法或类是否带有@ResponseEncrypt注解，且响应类型是否支持加密
	 * @param methodParameter 方法参数
	 * @param converterType 消息转换器类型
	 * @return 如果支持加密处理返回true，否则返回false
	 */
	@Override
	public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
		Method method = methodParameter.getMethod();
		Class<?> declaringClass = method.getDeclaringClass();
		return (declaringClass.isAnnotationPresent(ResponseEncrypt.class)
				|| method.isAnnotationPresent(ResponseEncrypt.class))
				&& secretHandler.supportResType(methodParameter.getParameterType());
	}

	/**
	 * 在响应体写入前进行加密处理 如果响应体不为空，则调用SecretHandler进行加密处理
	 * @param body 响应体对象
	 * @param methodParameter 方法参数
	 * @param selectedContentType 选中的内容类型
	 * @param selectedConverterType 选中的转换器类型
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return 加密后的响应体对象
	 */
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		if (body == null) {
			return null;
		}
		return secretHandler.encryptResBody(body, request, response);
	}

}

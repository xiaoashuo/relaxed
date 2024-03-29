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
 * @author Yakir
 * @Topic DecryptRequestAdvice
 * @Description
 * @date 2021/11/15 10:17
 * @Version 1.0
 */
@RequiredArgsConstructor
@RestControllerAdvice
public class EncryptResponseAdvice implements ResponseBodyAdvice<Object> {

	private final SecretHandler secretHandler;

	@Override
	public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
		Method method = methodParameter.getMethod();
		Class<?> declaringClass = method.getDeclaringClass();
		return (declaringClass.isAnnotationPresent(ResponseEncrypt.class)
				|| method.isAnnotationPresent(ResponseEncrypt.class))
				&& secretHandler.supportResType(methodParameter.getParameterType());
	}

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

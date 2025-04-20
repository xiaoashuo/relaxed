package com.relaxed.common.secret.config;

import com.relaxed.common.secret.annotation.RequestDecrypt;
import com.relaxed.common.secret.handler.SecretHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * 请求解密处理器 用于处理带有@RequestDecrypt注解的请求，支持请求体解密
 * 实现了RequestBodyAdviceAdapter接口，支持在消息转换器前后进行解密处理
 *
 * @author Yakir
 * @since 1.0
 */
@RequiredArgsConstructor
@RestControllerAdvice
public class DecryptRequestAdvice extends RequestBodyAdviceAdapter {

	private final SecretHandler secretHandler;

	/**
	 * 判断是否支持对当前请求进行解密处理 检查方法或类是否带有@RequestDecrypt注解，且请求类型是否支持解密
	 * @param methodParameter 方法参数
	 * @param targetType 目标类型
	 * @param converterType 消息转换器类型
	 * @return 如果支持解密处理返回true，否则返回false
	 */
	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {

		Method method = methodParameter.getMethod();
		Class<?> declaringClass = method.getDeclaringClass();
		return (declaringClass.isAnnotationPresent(RequestDecrypt.class)
				|| method.isAnnotationPresent(RequestDecrypt.class))
				&& secretHandler.supportReqType(methodParameter.getParameterType());
	}

	/**
	 * 在请求体被转换后处理参数 如果配置了post=true，则对已转换的请求体进行解密处理
	 * @param body 请求体对象
	 * @param inputMessage 输入消息
	 * @param methodParameter 方法参数
	 * @param targetType 目标类型
	 * @param converterType 转换器类型
	 * @return 处理后的请求体对象
	 */
	@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter methodParameter,
			Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		RequestDecrypt methodAnnotation = methodParameter.getMethodAnnotation(RequestDecrypt.class);
		if (methodAnnotation == null) {
			methodAnnotation = methodParameter.getDeclaringClass().getAnnotation(RequestDecrypt.class);
		}
		if (methodAnnotation.post()) {
			body = secretHandler.afterBodyRead(body);
		}

		return super.afterBodyRead(body, inputMessage, methodParameter, targetType, converterType);
	}

	/**
	 * 在请求体被转换前处理输入流 如果配置了pre=true，则对原始请求流进行解密处理
	 * @param inputMessage 输入消息
	 * @param methodParameter 方法参数
	 * @param targetType 目标类型
	 * @param converterType 转换器类型
	 * @return 处理后的输入消息
	 * @throws IOException 如果处理输入流时发生IO异常
	 */
	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter methodParameter,
			Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
		RequestDecrypt methodAnnotation = methodParameter.getMethodAnnotation(RequestDecrypt.class);
		if (methodAnnotation == null) {
			methodAnnotation = methodParameter.getDeclaringClass().getAnnotation(RequestDecrypt.class);
		}
		if (methodAnnotation.pre()) {
			HttpHeaders headers = inputMessage.getHeaders();
			InputStream body = inputMessage.getBody();
			String content = StreamUtils.copyToString(body, Charset.defaultCharset());
			String decryptContent = secretHandler.beforeBodyRead(content);
			// 返回处理后的消息体给messageConvert
			return new SecretHttpMessage(new ByteArrayInputStream(decryptContent.getBytes()), headers);
		}

		return inputMessage;
	}

}

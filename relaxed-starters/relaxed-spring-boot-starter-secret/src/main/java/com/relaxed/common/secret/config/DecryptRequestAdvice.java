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
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * @author Yakir
 * @Topic DecryptRequestAdvice
 * @Description 参考AbstractMessageConverterMethodArgumentResolver#readWithMessageConverters
 * Line 185
 * @date 2021/11/15 10:17
 * @Version 1.0
 */
@RequiredArgsConstructor
@RestControllerAdvice
public class DecryptRequestAdvice extends RequestBodyAdviceAdapter {

	private final SecretHandler secretHandler;

	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return methodParameter.getMethod().isAnnotationPresent(RequestDecrypt.class)
				&& secretHandler.supportType(methodParameter.getParameterType());
	}

	/**
	 * 在请求体 已经被转换过后 进行参数的二次处理
	 * @author yakir
	 * @date 2021/11/15 15:48
	 * @param body
	 * @param inputMessage
	 * @param methodParameter
	 * @param targetType
	 * @param converterType
	 * @return java.lang.Object
	 */
	@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter methodParameter,
			Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		RequestDecrypt methodAnnotation = methodParameter.getMethodAnnotation(RequestDecrypt.class);
		if (methodAnnotation.post()) {
			body = secretHandler.decryptReqBody(body);
		}
		return super.afterBodyRead(body, inputMessage, methodParameter, targetType, converterType);
	}

	/**
	 * 在请求体 未被转换前 把流字节进行处理
	 * @author yakir
	 * @date 2021/11/15 15:48
	 * @param inputMessage
	 * @param methodParameter
	 * @param targetType
	 * @param converterType
	 * @return org.springframework.http.HttpInputMessage
	 */
	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter methodParameter,
			Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
		RequestDecrypt methodAnnotation = methodParameter.getMethodAnnotation(RequestDecrypt.class);
		if (methodAnnotation.pre()) {
			HttpHeaders headers = inputMessage.getHeaders();
			InputStream body = inputMessage.getBody();
			String content = StreamUtils.copyToString(body, Charset.defaultCharset());
			String decryptContent = secretHandler.decryptReqBody(content);
			// 返回处理后的消息体给messageConvert
			return new SecretHttpMessage(new ByteArrayInputStream(decryptContent.getBytes()), headers);
		}

		return inputMessage;
	}

}

package com.relaxed.oauth2.auth.exception;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * 自定义OAuth2异常 扩展Spring Security OAuth2的异常处理 使用自定义的序列化器进行JSON序列化
 *
 * @author Yakir
 * @since 1.0
 */
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = CustomOAuth2ExceptionJackson2Serializer.class)
public class CustomOAuth2Exception extends OAuth2Exception {

	/**
	 * 构造函数
	 * @param msg 异常消息
	 * @param t 原始异常
	 */
	public CustomOAuth2Exception(String msg, Throwable t) {
		super(msg, t);
	}

	/**
	 * 构造函数
	 * @param msg 异常消息
	 */
	public CustomOAuth2Exception(String msg) {
		super(msg);
	}

}

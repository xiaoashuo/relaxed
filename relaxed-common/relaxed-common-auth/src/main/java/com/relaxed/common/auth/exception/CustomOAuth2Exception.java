package com.relaxed.common.auth.exception;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @author Yakir
 * @Topic CustomOAuth2Exception
 * @Description
 * @date 2022/7/22 15:31
 * @Version 1.0
 */
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = CustomOAuth2ExceptionJackson2Serializer.class)
public class CustomOAuth2Exception extends OAuth2Exception {

	public CustomOAuth2Exception(String msg, Throwable t) {
		super(msg, t);
	}

	public CustomOAuth2Exception(String msg) {
		super(msg);
	}

}

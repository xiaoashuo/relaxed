package com.relaxed.common.security.jwt.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Yakir
 * @Topic JwtAuthenticationException
 * @Description
 * @date 2021/8/15 10:17
 * @Version 1.0
 */
public class JwtVerifyException extends AuthenticationException {

	/**
	 * Constructs an {@code AuthenticationException} with the specified message and root
	 * cause.
	 * @param msg the detail message
	 * @param cause the root cause
	 */
	public JwtVerifyException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Constructs an {@code AuthenticationException} with the specified message and no
	 * root cause.
	 * @param msg the detail message
	 */
	public JwtVerifyException(String msg) {
		super(msg);
	}

}

package com.relaxed.common.oss.s3.exception;

import java.text.MessageFormat;

/**
 * OSS 操作异常类。 用于封装 OSS 操作过程中可能出现的异常，支持： 1. 简单的错误消息 2. 带原因的异常 3. 带格式化参数的异常消息
 *
 * @author Yakir
 * @since 1.0
 */
public class OssException extends RuntimeException {

	/**
	 * 使用指定的错误消息构造异常
	 * @param message 错误消息
	 */
	public OssException(String message) {
		super(message);
	}

	/**
	 * 使用指定的错误消息和原因构造异常
	 * @param message 错误消息
	 * @param cause 异常原因
	 */
	public OssException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 使用指定的原因、格式化消息和参数构造异常
	 * @param cause 异常原因
	 * @param message 格式化消息模板
	 * @param args 格式化参数
	 */
	public OssException(Throwable cause, String message, Object... args) {
		super(String.format(message, args), cause);
	}

}

package com.relaxed.common.oss.s3.exception;

import java.text.MessageFormat;

/**
 * @author Yakir
 * @Topic OssException
 * @Description
 * @date 2022/1/14 13:51
 * @Version 1.0
 */
public class OssException extends RuntimeException {

	public OssException(String message) {
		super(message);
	}

	public OssException(String message, Throwable cause) {
		super(message, cause);
	}

	public OssException(Throwable cause, String message, Object... args) {
		super(String.format(message, args), cause);
	}

}

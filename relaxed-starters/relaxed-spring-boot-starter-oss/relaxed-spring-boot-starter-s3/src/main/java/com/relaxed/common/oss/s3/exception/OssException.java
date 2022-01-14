package com.relaxed.common.oss.s3.exception;

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

}

package com.relaxed.common.http.exception;

/**
 * @author Yakir
 * @Topic RequestException
 * @Description
 * @date 2022/5/18 9:20
 * @Version 1.0
 */
public class RequestException extends RuntimeException {

	public RequestException(String message) {
		super(message);
	}

	public RequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public RequestException(Throwable cause) {
		super(cause);
	}

	protected RequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

package com.relaxed.common.http.exception;

/**
 * @author Yakir
 * @Topic ClientException
 * @Description
 * @date 2022/9/15 13:46
 * @Version 1.0
 */
public class ClientException extends RuntimeException {

	private final int code;

	private final String message;

	public ClientException(int code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}

	public ClientException(int code, String message, Throwable throwable) {
		super(message, throwable);
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

}

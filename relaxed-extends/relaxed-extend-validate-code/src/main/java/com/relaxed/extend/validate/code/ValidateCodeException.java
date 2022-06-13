package com.relaxed.extend.validate.code;

/**
 * @author Yakir
 * @Topic ValidateCodeException
 * @Description
 * @date 2022/6/12 15:53
 * @Version 1.0
 */
public class ValidateCodeException extends RuntimeException {

	public ValidateCodeException(String message) {
		super(message);
	}

	public ValidateCodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidateCodeException(Throwable cause) {
		super(cause);
	}

}

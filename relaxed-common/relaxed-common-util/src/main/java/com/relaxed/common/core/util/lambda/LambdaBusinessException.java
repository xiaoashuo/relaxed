package com.relaxed.common.core.util.lambda;

/**
 * @author Yakir
 * @Topic LambdaBusinessException
 * @Description
 * @date 2022/6/14 16:42
 * @Version 1.0
 */
public class LambdaBusinessException extends RuntimeException {

	public LambdaBusinessException(String message) {
		super(message);
	}

	public LambdaBusinessException(Throwable cause) {
		super(cause);
	}

}

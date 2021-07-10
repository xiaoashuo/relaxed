package com.relaxed.common.core.batch;

/**
 * @author Yakir
 * @Topic BatchException
 * @Description
 * @date 2021/7/10 8:44
 * @Version 1.0
 */
public class BatchException extends RuntimeException {

	public BatchException(String message) {
		super(message);
	}

	public BatchException(String message, Throwable cause) {
		super(message, cause);
	}

}

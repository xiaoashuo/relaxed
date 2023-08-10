package com.relaxed.common.datetime.holidays;

/**
 * @author Yakir
 * @Topic DateTimeException
 * @Description
 * @date 2023/8/10 10:16
 * @Version 1.0
 */
public class DateTimeException extends RuntimeException {

	public DateTimeException() {
	}

	public DateTimeException(String message) {
		super(message);
	}

	public DateTimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public DateTimeException(Throwable cause) {
		super(cause);
	}

	public DateTimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

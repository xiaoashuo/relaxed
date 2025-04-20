package com.relaxed.common.log.biz.exception;

/**
 * 业务日志记录异常类，用于封装日志记录过程中可能出现的各种异常情况。 该异常类继承自 {@link RuntimeException}，表示日志记录过程中的非受检异常。
 * 主要用于以下场景： 1. 日志模板解析失败 2. 函数执行异常 3. 日志记录服务异常 4. 其他与日志记录相关的异常情况
 *
 * @author Yakir
 * @since 1.0.0
 */
public class LogRecordException extends RuntimeException {

	/**
	 * 使用指定的错误消息构造异常
	 * @param message 错误消息
	 */
	public LogRecordException(String message) {
		super(message);
	}

	/**
	 * 使用指定的错误消息和原因构造异常
	 * @param message 错误消息
	 * @param cause 异常原因
	 */
	public LogRecordException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 使用指定的原因构造异常
	 * @param cause 异常原因
	 */
	public LogRecordException(Throwable cause) {
		super(cause);
	}

	/**
	 * 使用指定的错误消息、原因、是否启用抑制和是否可写堆栈跟踪构造异常
	 * @param message 错误消息
	 * @param cause 异常原因
	 * @param enableSuppression 是否启用抑制
	 * @param writableStackTrace 是否可写堆栈跟踪
	 */
	public LogRecordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

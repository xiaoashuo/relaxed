package com.relaxed.poi.exception;

/**
 * Word文档处理异常类，用于处理Word文档操作过程中可能出现的各种异常情况。
 *
 * @author Yakir
 * @since 1.0.0
 */
public class WordException extends RuntimeException {

	/**
	 * 构造一个新的Word异常实例
	 */
	public WordException() {
		super();
	}

	/**
	 * 使用指定的错误消息构造一个新的Word异常实例
	 * @param message 错误消息
	 */
	public WordException(String message) {
		super(message);
	}

	/**
	 * 使用指定的错误消息和原因构造一个新的Word异常实例
	 * @param message 错误消息
	 * @param cause 异常原因
	 */
	public WordException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 使用指定的原因构造一个新的Word异常实例
	 * @param cause 异常原因
	 */
	public WordException(Throwable cause) {
		super(cause);
	}

	/**
	 * 使用指定的错误消息、原因、是否启用抑制和是否可写堆栈跟踪构造一个新的Word异常实例
	 * @param message 错误消息
	 * @param cause 异常原因
	 * @param enableSuppression 是否启用抑制
	 * @param writableStackTrace 是否可写堆栈跟踪
	 */
	protected WordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

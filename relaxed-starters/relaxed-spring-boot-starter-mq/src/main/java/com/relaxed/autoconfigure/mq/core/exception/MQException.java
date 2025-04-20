package com.relaxed.autoconfigure.mq.core.exception;

/**
 * 消息队列异常类。 用于表示消息队列操作过程中发生的异常，继承自RuntimeException。 支持多种异常构造方式，包括消息、原因和堆栈跟踪控制。
 *
 * @author Yakir
 * @since 1.0
 */
public class MQException extends RuntimeException {

	/**
	 * 创建一个新的MQException实例。
	 */
	public MQException() {
	}

	/**
	 * 使用指定的错误消息创建一个新的MQException实例。
	 * @param message 错误消息
	 */
	public MQException(String message) {
		super(message);
	}

	/**
	 * 使用指定的错误消息和原因创建一个新的MQException实例。
	 * @param message 错误消息
	 * @param cause 异常原因
	 */
	public MQException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 使用指定的原因创建一个新的MQException实例。
	 * @param cause 异常原因
	 */
	public MQException(Throwable cause) {
		super(cause);
	}

	/**
	 * 使用指定的错误消息、原因、抑制标志和可写堆栈跟踪标志创建一个新的MQException实例。
	 * @param message 错误消息
	 * @param cause 异常原因
	 * @param enableSuppression 是否启用抑制
	 * @param writableStackTrace 是否可写堆栈跟踪
	 */
	public MQException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

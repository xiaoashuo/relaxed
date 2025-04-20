package com.relaxed.common.datetime.holidays;

/**
 * 日期时间异常类 用于处理日期时间计算过程中的异常情况，例如：
 * <ul>
 * <li>开始日期大于结束日期</li>
 * <li>日期格式不正确</li>
 * <li>日期计算结果超出有效范围</li>
 * </ul>
 * 此异常类继承自 {@link RuntimeException}，为非受检异常。
 *
 * @author Yakir
 * @since 1.0.0
 */
public class DateTimeException extends RuntimeException {

	/**
	 * 构造一个没有详细消息的 DateTimeException
	 */
	public DateTimeException() {
	}

	/**
	 * 构造一个带有详细消息的 DateTimeException
	 * @param message 异常的详细消息
	 */
	public DateTimeException(String message) {
		super(message);
	}

	/**
	 * 构造一个带有详细消息和原因的 DateTimeException
	 * @param message 异常的详细消息
	 * @param cause 异常的原因
	 */
	public DateTimeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 构造一个带有原因的 DateTimeException
	 * @param cause 异常的原因
	 */
	public DateTimeException(Throwable cause) {
		super(cause);
	}

	/**
	 * 构造一个带有所有参数的 DateTimeException
	 * @param message 异常的详细消息
	 * @param cause 异常的原因
	 * @param enableSuppression 是否启用异常抑制
	 * @param writableStackTrace 是否生成栈追踪
	 */
	public DateTimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

package com.relaxed.common.exception;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 异常处理配置类
 * <p>
 * 用于配置异常处理的相关参数，包括忽略的异常类型、通知间隔时间、消息阈值等。 这些配置会影响异常通知的行为和频率。
 * </p>
 *
 * @author lingting
 * @since 1.0.0
 */
@Data
public class ExceptionHandleConfig {

	/**
	 * 忽略的异常类型集合
	 * <p>
	 * 指定需要忽略的异常类型，系统将不会对这些异常进行通知。 注意：只会忽略指定的异常类，而不会忽略该异常类的子类。
	 * </p>
	 */
	private Set<Class<? extends Throwable>> ignoreExceptions = new HashSet<>();

	/**
	 * 异常通知间隔时间
	 * <p>
	 * 单位：秒，默认值为5分钟。 表示在发送一次异常通知后，需要等待的时间间隔。
	 * </p>
	 */
	private long time = TimeUnit.MINUTES.toSeconds(5);

	/**
	 * 异常通知阈值
	 * <p>
	 * 即使未达到通知间隔时间，如果异常发生的数量达到此阈值， 也会立即发送异常通知。
	 * </p>
	 */
	private int max = 5;

	/**
	 * 异常堆栈信息长度限制
	 * <p>
	 * 控制异常堆栈信息转换为字符串时的最大长度。 超出此长度的堆栈信息将被截断。
	 * </p>
	 */
	private int length = 3000;

}

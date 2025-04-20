package com.relaxed.common.exception.handler;

import com.relaxed.common.exception.domain.ExceptionMessage;
import com.relaxed.common.exception.domain.ExceptionNoticeResponse;

/**
 * 全局异常处理器接口
 * <p>
 * 定义了处理异常的统一接口，用于实现异常的统一处理逻辑。 实现类可以自定义异常处理方式，如记录日志、发送通知等。
 * </p>
 *
 * @author Hccake
 * @since 1.0.0
 */
public interface GlobalExceptionHandler {

	/**
	 * 处理异常信息
	 * <p>
	 * 实现类可以在此方法中定义具体的异常处理逻辑， 如记录日志、发送通知、存储异常信息等。
	 * </p>
	 * @param throwable 需要处理的异常对象
	 */
	void handle(Throwable throwable);

}

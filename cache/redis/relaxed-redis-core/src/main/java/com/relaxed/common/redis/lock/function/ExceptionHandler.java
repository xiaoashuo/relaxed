package com.relaxed.common.redis.lock.function;

/**
 * 异常处理器接口。 用于处理分布式锁操作过程中可能出现的异常，支持自定义异常处理逻辑。
 *
 * @author hccake
 * @since 1.0
 */
@FunctionalInterface
public interface ExceptionHandler {

	/**
	 * 处理异常
	 * @param throwable 需要处理的异常对象
	 */
	void handle(Throwable throwable);

}

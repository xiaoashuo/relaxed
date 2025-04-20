package com.relaxed.common.redis.lock.function;

/**
 * 可抛出异常的执行器接口。 用于封装可能抛出异常的操作，支持泛型返回值。
 *
 * @param <T> 执行结果的类型
 * @author huyuanzhi
 * @since 1.0
 */
public interface ThrowingExecutor<T> {

	/**
	 * 执行操作
	 * @return 操作执行结果
	 * @throws Throwable 执行过程中可能抛出的异常
	 */
	T execute() throws Throwable;

}

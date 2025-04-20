package com.relaxed.common.redis.lock;

import com.relaxed.common.redis.lock.function.ThrowingExecutor;

/**
 * 分布式锁操作接口。 定义获取锁后执行的操作，支持泛型返回值。
 *
 * @param <T> 操作返回值的类型
 * @author huyuanzhi
 * @since 1.0
 */
public interface Action<T> {

	/**
	 * 执行加锁后的操作
	 * @param lockKey 分布式锁的键名
	 * @param supplier 需要执行的操作
	 * @return 操作执行状态处理器
	 */
	StateHandler<T> action(String lockKey, ThrowingExecutor<T> supplier);

}

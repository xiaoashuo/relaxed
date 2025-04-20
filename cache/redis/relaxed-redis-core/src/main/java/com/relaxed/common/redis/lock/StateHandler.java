package com.relaxed.common.redis.lock;

import com.relaxed.common.redis.lock.function.ExceptionHandler;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * 分布式锁状态处理器接口。 用于处理分布式锁的各种状态和回调，支持链式调用。
 *
 * @param <T> 操作返回值的类型
 * @author huyuanzhi
 * @since 1.0
 */
public interface StateHandler<T> {

	/**
	 * 设置锁的超时时间
	 * @param lockTimeOut 锁的超时时间
	 * @return 状态处理器
	 */
	StateHandler<T> lockTimeOut(long lockTimeOut);

	/**
	 * 设置锁的时间单位
	 * @param timeUnit 时间单位
	 * @return 状态处理器
	 */
	StateHandler<T> lockTimeUnit(TimeUnit timeUnit);

	/**
	 * 设置获取锁成功且业务方法执行成功的回调
	 * @param action 回调方法，接收执行结果并返回处理后的结果
	 * @return 状态处理器
	 */
	StateHandler<T> onSuccess(UnaryOperator<T> action);

	/**
	 * 设置获取锁失败的回调
	 * @param action 回调方法，返回替代结果
	 * @return 状态处理器
	 */
	StateHandler<T> onLockFail(Supplier<T> action);

	/**
	 * 设置获取锁成功但执行业务方法异常的回调
	 * @param action 异常处理方法
	 * @return 状态处理器
	 */
	StateHandler<T> onException(ExceptionHandler action);

	/**
	 * 执行加锁操作
	 * @return 操作执行结果
	 */
	T lock();

}

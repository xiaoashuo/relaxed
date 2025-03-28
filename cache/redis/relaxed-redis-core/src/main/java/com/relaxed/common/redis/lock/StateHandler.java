package com.relaxed.common.redis.lock;

import com.relaxed.common.redis.lock.function.ExceptionHandler;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * @author huyuanzhi 状态处理器
 * @param <T> 返回类型
 */
public interface StateHandler<T> {

	/**
	 * 锁超时时间
	 * @date 2022/10/12 18:57
	 * @param lockTimeOut
	 * @return com.relaxed.common.redis.lock.StateHandler<T>
	 */
	StateHandler<T> lockTimeOut(long lockTimeOut);

	/**
	 * 锁时间单位
	 * @param timeUnit
	 * @return
	 */
	StateHandler<T> lockTimeUnit(TimeUnit timeUnit);

	/**
	 * 获取锁成功，业务方法执行成功回调
	 * @param action 回调方法引用
	 * @return 状态处理器
	 */
	StateHandler<T> onSuccess(UnaryOperator<T> action);

	/**
	 * 获取锁失败回调
	 * @param action 回调方法引用
	 * @return 状态处理器
	 */
	StateHandler<T> onLockFail(Supplier<T> action);

	/**
	 * 获取锁成功，执行业务方法异常回调
	 * @param action 回调方法引用
	 * @return 状态处理器
	 */
	StateHandler<T> onException(ExceptionHandler action);

	/**
	 * 终态，获取锁
	 * @return result
	 */
	T lock();

}

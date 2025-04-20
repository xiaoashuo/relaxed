package com.relaxed.common.redis.lock;

import cn.hutool.core.lang.Assert;
import com.relaxed.common.redis.lock.function.ExceptionHandler;
import com.relaxed.common.redis.lock.function.ThrowingExecutor;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * 分布式锁实现类。 提供基于Redis的分布式锁功能，支持锁超时、异常处理和回调。 实现了Action和StateHandler接口，支持链式调用。
 *
 * @param <T> 操作返回值的类型
 * @author huyuanzhi
 * @since 1.0
 */
public final class DistributedLock<T> implements Action<T>, StateHandler<T> {

	/**
	 * 操作执行结果
	 */
	private T result;

	/**
	 * 锁的键名
	 */
	private String key;

	/**
	 * 需要执行的操作
	 */
	private ThrowingExecutor<T> executeAction;

	/**
	 * 成功回调
	 */
	private UnaryOperator<T> successAction;

	/**
	 * 获取锁失败回调
	 */
	private Supplier<T> lockFailAction;

	/**
	 * 锁管理器
	 */
	private LockManage lockManage;

	/**
	 * 锁的超时时间，默认24小时
	 */
	private Long lockTimeOut = 86400L;

	/**
	 * 时间单位，默认秒
	 */
	private TimeUnit timeUnit = TimeUnit.SECONDS;

	/**
	 * 异常处理器，默认抛出异常
	 */
	private ExceptionHandler exceptionHandler = DistributedLock::throwException;

	/**
	 * 获取分布式锁实例
	 * @param <T> 操作返回值的类型
	 * @return 分布式锁实例
	 */
	public static <T> Action<T> instance() {
		return new DistributedLock<>();
	}

	@Override
	public StateHandler<T> lockTimeOut(long lockTimeOut) {
		this.lockTimeOut = lockTimeOut;
		return this;
	}

	@Override
	public StateHandler<T> lockTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
		return this;
	}

	@Override
	public StateHandler<T> action(String lockKey, ThrowingExecutor<T> action) {
		Assert.isTrue(this.executeAction == null, "execute action has been already set");
		Assert.notNull(action, "execute action cant be null");
		Assert.notBlank(lockKey, "lock key cant be blank");
		this.executeAction = action;
		this.key = lockKey;
		return this;
	}

	@Override
	public StateHandler<T> onSuccess(UnaryOperator<T> action) {
		Assert.isTrue(this.successAction == null, "success action has been already set");
		Assert.notNull(action, "success action cant be null");
		this.successAction = action;
		return this;
	}

	@Override
	public StateHandler<T> onLockFail(Supplier<T> action) {
		Assert.isTrue(this.lockFailAction == null, "lock fail action has been already set");
		Assert.notNull(action, "lock fail action cant be null");
		this.lockFailAction = action;
		return this;
	}

	@Override
	public StateHandler<T> onException(ExceptionHandler exceptionHandler) {
		Assert.notNull(exceptionHandler, "exception handler cant be null");
		this.exceptionHandler = exceptionHandler;
		return this;
	}

	@Override
	public T lock() {
		String requestId = UUID.randomUUID().toString();
		if (Boolean.TRUE.equals(lockManage.lock(this.key, requestId, lockTimeOut, timeUnit))) {
			T value = null;
			boolean exResolved = false;
			try {
				value = executeAction.execute();
				this.result = value;
			}
			catch (Throwable e) {
				this.exceptionHandler.handle(e);
				exResolved = true;
			}
			finally {
				lockManage.releaseLock(this.key, requestId);
			}
			if (!exResolved && this.successAction != null) {
				this.result = this.successAction.apply(value);
			}
		}
		else if (lockFailAction != null) {
			this.result = lockFailAction.get();
		}
		return this.result;
	}

	/**
	 * 抛出异常
	 * @param t 异常对象
	 * @param <E> 异常类型
	 * @throws E 抛出指定类型的异常
	 */
	@SuppressWarnings("unchecked")
	private static <E extends Throwable> void throwException(Throwable t) throws E {
		throw (E) t;
	}

}

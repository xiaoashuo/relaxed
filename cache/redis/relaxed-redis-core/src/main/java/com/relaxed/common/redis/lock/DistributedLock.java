package com.relaxed.common.redis.lock;

import cn.hutool.core.lang.Assert;
import com.relaxed.common.redis.lock.function.ExceptionHandler;
import com.relaxed.common.redis.lock.function.ThrowingExecutor;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * @author huyuanzhi
 * @version 1.0
 * @date 2021/11/16 分布式锁操作类
 */
public final class DistributedLock<T> implements Action<T>, StateHandler<T> {

	T result;

	String key;

	ThrowingExecutor<T> executeAction;

	UnaryOperator<T> successAction;

	Supplier<T> lockFailAction;

	LockManage lockManage;

	Long lockTimeOut = 86400L;

	TimeUnit timeUnit = TimeUnit.SECONDS;

	ExceptionHandler exceptionHandler = DistributedLock::throwException;

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

	@SuppressWarnings("unchecked")
	private static <E extends Throwable> void throwException(Throwable t) throws E {
		throw (E) t;
	}

}

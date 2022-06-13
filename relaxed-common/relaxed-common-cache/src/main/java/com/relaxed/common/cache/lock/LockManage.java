package com.relaxed.common.cache.lock;

import com.relaxed.common.cache.generate.KeyGenerator;

import com.relaxed.common.cache.lock.operator.LockOperator;
import lombok.RequiredArgsConstructor;

/**
 * @author Yakir
 * @Topic CacheLockManage
 * @Description
 * @date 2022/6/13 11:39
 * @Version 1.0
 */
@RequiredArgsConstructor
public class LockManage<T> implements LockOperator<T> {

	private final LockOperator lockOperator;

	/** 缓存key生成器 **/
	protected final KeyGenerator keyGenerator;

	public boolean lock(String key, T requestId) {
		return lockOperator.lock(buildKey(key), requestId);
	}

	public boolean lock(String key, T requestId, Long ttl) {
		return lockOperator.lock(buildKey(key), requestId, ttl);
	}

	public boolean lock(String key, T requestId, Long ttl, long timeout) {
		return lockOperator.lock(buildKey(key), requestId, ttl, timeout);
	}

	public boolean releaseLock(String key, T requestId) {
		return lockOperator.releaseLock(buildKey(key), requestId);
	}

	public String buildKey(String originalText) {
		return keyGenerator.generate(originalText);
	}

}

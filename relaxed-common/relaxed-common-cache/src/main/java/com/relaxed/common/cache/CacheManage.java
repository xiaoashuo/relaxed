package com.relaxed.common.cache;

import com.relaxed.common.cache.operator.CacheOperator;
import com.relaxed.common.cache.generate.KeyGenerator;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Yakir
 * @Topic CacheManage
 * @Description
 * @date 2022/6/13 10:33
 * @Version 1.0
 */
@RequiredArgsConstructor
public class CacheManage<T> {

	/** 缓存操作者 **/
	protected final CacheOperator cacheOperator;

	/** 缓存key生成器 **/
	protected final KeyGenerator keyGenerator;

	public void set(String key, T value) {
		cacheOperator.set(buildKey(key), value);
	}

	public void set(String key, T value, long time, TimeUnit unit) {
		cacheOperator.set(buildKey(key), value, time, unit);
	}

	public T get(String key) {
		return (T) cacheOperator.get(buildKey(key));
	}

	public void remove(String key) {
		cacheOperator.remove(buildKey(key));
	}

	public void remove(Collection<String> keys) {
		Set<String> cacheKeys = new HashSet<>();
		for (String key : keys) {
			cacheKeys.add(buildKey(key));
		}
		cacheOperator.remove(cacheKeys);
	}

	public void expire(String key, long time, TimeUnit unit) {
		cacheOperator.expire(buildKey(key), time, unit);
	}

	public boolean contains(String key) {
		return cacheOperator.contains(buildKey(key));
	}

	public String buildKey(String originalText) {
		return keyGenerator.generate(originalText);
	}

}

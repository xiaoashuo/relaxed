package com.relaxed.common.cache;

import com.relaxed.common.cache.operator.CacheOperator;
import com.relaxed.common.cache.generate.KeyGenerator;
import lombok.RequiredArgsConstructor;

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
public class CacheManage<T> implements CacheOperator<T> {

	/** 缓存操作者 **/
	protected final CacheOperator cacheOperator;

	/** 缓存key生成器 **/
	protected final KeyGenerator keyGenerator;

	@Override
	public void set(String key, T value) {
		cacheOperator.set(buildKey(key), value);
	}

	@Override
	public void set(String key, T value, long time, TimeUnit unit) {
		cacheOperator.set(buildKey(key), value, time, unit);
	}

	@Override
	public T get(String key) {
		return (T) cacheOperator.get(buildKey(key));
	}

	@Override
	public void remove(String key) {
		cacheOperator.remove(buildKey(key));
	}

	@Override
	public void remove(String... keys) {
		Set<String> cacheKeys = new HashSet<>();
		for (String key : keys) {
			cacheKeys.add(buildKey(key));
		}
		cacheOperator.remove(cacheKeys.toArray(new String[0]));
	}

	@Override
	public void expire(String key, long time, TimeUnit unit) {
		cacheOperator.expire(buildKey(key), time, unit);
	}

	@Override
	public boolean contains(String key) {
		return cacheOperator.contains(buildKey(key));
	}

	public String buildKey(String originalText) {
		return keyGenerator.generate(originalText);
	}

}

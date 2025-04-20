package com.relaxed.common.idempotent.key;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;

import java.util.concurrent.TimeUnit;

/**
 * 内存幂等键存储实现类。 使用 {@link TimedCache} 实现内存中的幂等键存储，主要功能包括： 1. 支持定时清理过期键 2. 支持并发安全 3.
 * 支持自定义过期时间
 *
 * @author Yakir
 * @since 1.0
 */
public class InMemoryIdempotentKeyStore extends AbstractIdempotentKeyStore {

	private final TimedCache<String, Long> cache;

	/**
	 * 创建内存幂等键存储实例。 初始化定时缓存，设置清理间隔为 1 秒。
	 */
	public InMemoryIdempotentKeyStore() {
		this.cache = CacheUtil.newTimedCache(Integer.MAX_VALUE);
		cache.schedulePrune(1);
	}

	/**
	 * 当不存在有效 key 时将其存储下来。 使用同步机制确保并发安全。
	 * @param key 幂等键
	 * @param duration key 的有效时长
	 * @param timeUnit 时长单位
	 * @return true 表示存储成功，false 表示存储失败
	 */
	@Override
	public synchronized boolean saveIfAbsent(String key, long duration, TimeUnit timeUnit) {
		Long value = cache.get(key, false);
		if (value == null) {
			long timeOut = TimeUnit.MILLISECONDS.convert(duration, timeUnit);
			cache.put(key, System.currentTimeMillis(), timeOut);
			return true;
		}
		return false;
	}

	/**
	 * 删除指定的幂等键。
	 * @param key 幂等键
	 */
	@Override
	public void remove(String key) {
		cache.remove(key);
	}

}

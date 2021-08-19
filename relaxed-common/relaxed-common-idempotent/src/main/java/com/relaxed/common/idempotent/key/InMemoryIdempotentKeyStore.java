package com.relaxed.common.idempotent.key;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;

/**
 * @author hccake
 */
public class InMemoryIdempotentKeyStore extends AbstractIdempotentKeyStore {

	private final TimedCache<String, Long> cache;

	public InMemoryIdempotentKeyStore() {
		this.cache = CacheUtil.newTimedCache(Integer.MAX_VALUE);
		cache.schedulePrune(1);
	}

	@Override
	public synchronized boolean saveIfAbsent(String key, long duration) {
		Long value = cache.get(key, false);
		if (value == null) {
			cache.put(key, System.currentTimeMillis(), duration * 1000);
			return true;
		}
		return false;
	}

	@Override
	public void remove(String key) {
		cache.remove(key);
	}

}

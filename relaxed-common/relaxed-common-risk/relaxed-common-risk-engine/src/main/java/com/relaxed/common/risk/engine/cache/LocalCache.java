package com.relaxed.common.risk.engine.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

/**
 * @author Yakir
 * @Topic LocalCache
 * @Description
 * @date 2021/8/29 9:13
 * @Version 1.0
 */
@Slf4j
public class LocalCache implements CacheService {

	protected Cache localCache = Caffeine.newBuilder().maximumSize(64).expireAfterAccess(Duration.ofHours(1)).build();

	@Override
	public <V> V get(String key) {
		return (V) localCache.get(key, v -> v);
	}

	@Override
	public <V> void put(String key, V obj, Long expireTime) {
		localCache.put(key, obj);
	}

	@Override
	public void del(String key) {
		localCache.invalidate(key);
	}

}

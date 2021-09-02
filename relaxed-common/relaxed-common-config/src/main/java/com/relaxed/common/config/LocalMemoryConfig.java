package com.relaxed.common.config;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.CacheObj;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.lang.Dict;

import java.util.*;

/**
 * @author Yakir
 * @Topic MemoryConfig
 * @Description
 * @date 2021/9/2 9:54
 * @Version 1.0
 */
public class LocalMemoryConfig implements Config<String, Object> {

	private final TimedCache<String, Object> cache;

	public LocalMemoryConfig() {
		this.cache = CacheUtil.newTimedCache(Integer.MAX_VALUE);
		cache.schedulePrune(1);
	}

	@Override
	public void initConfig(Map<String, Object> configs) {
		if (configs == null || configs.size() == 0) {
			return;
		}
		for (Map.Entry<String, Object> entry : configs.entrySet()) {
			cache.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public Map<String, Object> getAllConfigs() {
		Map<String, Object> configs = new HashMap<>();
		Iterator<CacheObj<String, Object>> cacheObjIterator = cache.cacheObjIterator();
		while (cacheObjIterator.hasNext()) {
			CacheObj<String, Object> next = cacheObjIterator.next();
			configs.put(next.getKey(), next.getValue());
		}
		return configs;
	}

	@Override
	public Set<String> getAllConfigKeys() {
		return cache.keySet();
	}

	@Override
	public void put(String key, Object val) {
		cache.put(key, val);
	}

	@Override
	public void del(String key) {
		cache.remove(key);
	}

	@Override
	public Object get(String key) {
		return cache.get(key);
	}

	@Override
	public Object get(String key, Object defaultValue) {
		return Optional.ofNullable(cache.get(key)).orElseGet(() -> defaultValue);
	}

}

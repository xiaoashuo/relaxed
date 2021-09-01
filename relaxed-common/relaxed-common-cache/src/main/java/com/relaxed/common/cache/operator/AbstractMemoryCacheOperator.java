package com.relaxed.common.cache.operator;

import cn.hutool.cache.impl.TimedCache;
import com.relaxed.common.cache.CacheOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author Yakir
 * @Topic AbstractMemoryCacheOperator
 * @Description
 * @date 2021/9/1 16:57
 * @Version 1.0
 */
public abstract class AbstractMemoryCacheOperator<T> implements CacheOperator<T> {

	private final Logger log = LoggerFactory.getLogger(AbstractMemoryCacheOperator.class);

	private final TimedCache<String, T> timedCache;

	public AbstractMemoryCacheOperator(TimedCache<String, T> timedCache) {
		this.timedCache = timedCache;
	}

	@Override
	public void set(String key, T value) {
		timedCache.put(key, value);
	}

	@Override
	public void set(String key, T value, long time) {
		timedCache.put(key, value, time * 1000);
	}

	@Override
	public void set(String key, T value, long time, TimeUnit unit) {
		timedCache.put(key, value, unit.toMillis(time));

	}

	@Override
	public T get(String key) {
		return timedCache.get(key);
	}

	@Override
	public void remove(String key) {
		timedCache.remove(key);
	}

	@Override
	public void remove(String... key) {
		if (key.length > 0) {
			for (String itemKey : key) {
				timedCache.remove(itemKey);
			}
		}
	}

	@Override
	public void expire(String key, long time, TimeUnit unit) {
		T value = timedCache.get(key, true);
		timedCache.put(key, value, unit.toMillis(time));
	}

	@Override
	public boolean contains(String key) {
		return timedCache.containsKey(key);
	}

	@Override
	public synchronized boolean lock(String key, T requestId) {
		log.info("memory cache no implement...");
		return false;
	}

	@Override
	public synchronized boolean lock(String key, T requestId, Long ttl) {
		log.info("memory cache no implement...");
		return false;
	}

	@Override
	public synchronized boolean lock(String key, T requestId, Long ttl, long timeout) {
		log.info("memory cache no implement...");
		return false;
	}

	@Override
	public synchronized boolean releaseLock(String key, T requestId) {
		log.info("memory cache no implement...");
		return false;
	}

	@Override
	public <O> O getOperator() {
		return (O) timedCache;
	}

}

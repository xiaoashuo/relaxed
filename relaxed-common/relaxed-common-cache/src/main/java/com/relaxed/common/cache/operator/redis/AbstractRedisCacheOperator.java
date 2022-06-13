package com.relaxed.common.cache.operator.redis;

import com.relaxed.common.cache.CacheAction;
import com.relaxed.common.cache.operator.CacheOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author Yakir
 * @Topic AbstractRedisCacheOperator
 * @Description
 * @date 2021/9/1 16:56
 * @Version 1.0
 */
public class AbstractRedisCacheOperator<T> implements CacheOperator<T> {

	private final Logger log = LoggerFactory.getLogger(AbstractRedisCacheOperator.class);

	protected final RedisTemplate<String, T> redisTemplate;

	public AbstractRedisCacheOperator(RedisTemplate<String, T> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void set(String key, T value) {
		redisTemplate.opsForValue().set(key, value);
	}

	@Override
	public void set(String key, T value, long time, TimeUnit unit) {
		redisTemplate.opsForValue().set(key, value, time, unit);
	}

	@Override
	public T get(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	@Override
	public void remove(String key) {
		redisTemplate.delete(key);
	}

	@Override
	public void remove(String... key) {
		redisTemplate.delete(Arrays.asList(key));
	}

	@Override
	public void expire(String key, long time, TimeUnit unit) {
		redisTemplate.expire(key, time, unit);
	}

	@Override
	public boolean contains(String key) {
		return redisTemplate.opsForValue().get(key) != null;
	}

}

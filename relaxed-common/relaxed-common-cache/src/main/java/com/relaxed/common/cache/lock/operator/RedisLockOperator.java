package com.relaxed.common.cache.lock.operator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Yakir
 * @Topic AbstractLockRedisCacheOperator
 * @Description
 * @date 2022/6/13 10:57
 * @Version 1.0
 */
@RequiredArgsConstructor
@Slf4j
public class RedisLockOperator<T> implements LockOperator<T> {

	protected final RedisTemplate<String, T> redisTemplate;

	/**
	 *
	 * 释放锁lua脚本 KEYS【1】：key值是为要加的锁定义的字符串常量 ARGV【1】：value值是 request id, 用来防止解除了不该解除的锁. 可用
	 * UUID
	 */
	private static final String RELEASE_LOCK_LUA_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

	/**
	 * 释放锁成功返回值
	 */
	private static final Long RELEASE_LOCK_SUCCESS_RESULT = 1L;

	@Override
	public boolean lock(String key, T requestId) {
		return lock(key, requestId, -1L);
	}

	@Override
	public boolean lock(String key, T requestId, Long ttl) {
		log.trace("lock: {key:{}, clientId:{}}", key, requestId);
		if (ttl < 0) {
			return redisTemplate.opsForValue().setIfAbsent(key, requestId);
		}
		return redisTemplate.opsForValue().setIfAbsent(key, requestId, ttl, TimeUnit.SECONDS);

	}

	@Override
	public boolean lock(String key, T requestId, Long ttl, long timeout) {
		long startTime = System.currentTimeMillis();
		Boolean token;
		do {
			token = lock(key, requestId, ttl);
			if (!token) {
				// 当前时间-开始时间 是否大于超时时间之前50s
				if ((System.currentTimeMillis() - startTime) > (timeout - 50)) {
					break;
				}
				try {
					// try 50 per sec
					Thread.sleep(50);
				}
				catch (InterruptedException e) {
					log.error("lock获取线程终端异常", e);
					return false;
				}
			}
		}
		while (!token);

		return token;
	}

	@Override
	public boolean releaseLock(String key, T requestId) {
		log.trace("release lock: {key:{}, clientId:{}}", key, requestId);
		// 指定ReturnType为Long.class
		DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(RELEASE_LOCK_LUA_SCRIPT, Long.class);
		Long result = redisTemplate.execute(redisScript, Collections.singletonList(key), requestId);
		return Objects.equals(result, RELEASE_LOCK_SUCCESS_RESULT);
	}

}

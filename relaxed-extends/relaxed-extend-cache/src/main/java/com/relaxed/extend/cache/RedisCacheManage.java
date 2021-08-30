package com.relaxed.extend.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Yakir
 * @Topic RedisCacheLock
 * @Description
 * @date 2021/7/23 17:05
 * @Version 1.0
 */
@Slf4j
public class RedisCacheManage extends AbstractCacheManage {

	private static StringRedisTemplate redisTemplate;

	public void setStringRedisTemplate(StringRedisTemplate redisTemplate) {
		RedisCacheManage.redisTemplate = redisTemplate;
	}

	@Override
	public String get(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	@Override
	public void set(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
	}

	@Override
	public void set(String key, String value, long time, TimeUnit unit) {
		redisTemplate.opsForValue().set(key, value, time, unit);
	}

	@Override
	public Boolean delete(String key) {
		return redisTemplate.delete(key);
	}


	@Override
	public StringRedisTemplate getOperator() {
		return redisTemplate;
	}

	/**
	 * 上锁
	 * @param key 锁key
	 * @param requestId 请求id item
	 * @return Boolean 是否成功获取锁
	 */
	@Override
	public Boolean lock(String key, String requestId) {
		return lock(key, requestId, 1000L);
	}

	/**
	 * 上锁 指定过期时间
	 * @param key 锁key
	 * @param requestId 锁id item
	 * @param ttl 过期时间 /s 秒为单位
	 * @return Boolean 是否成功获取锁
	 */
	@Override
	public Boolean lock(String key, String requestId, Long ttl) {
		log.trace("lock: {key:{}, clientId:{}}", key, requestId);
		return redisTemplate.opsForValue().setIfAbsent(key, requestId, ttl, TimeUnit.SECONDS);
	}

	@Override
	public Boolean lock(String key, String requestId, Long ttl, long timeout) {
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

	/**
	 * 释放锁
	 * @param key 锁key
	 * @param requestId 请求id item
	 * @return Boolean 是否成功释放锁
	 */
	@Override
	public Boolean releaseLock(String key, String requestId) {
		log.trace("release lock: {key:{}, clientId:{}}", key, requestId);
		// 指定ReturnType为Long.class
		DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(RELEASE_LOCK_LUA_SCRIPT, Long.class);
		Long result = redisTemplate.execute(redisScript, Collections.singletonList(key), requestId);
		return Objects.equals(result, RELEASE_LOCK_SUCCESS_RESULT);
	}

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

}

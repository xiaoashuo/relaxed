package com.relaxed.common.redis.lock;

import com.relaxed.common.redis.RedisHelper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁管理器。 提供基于Redis的分布式锁的获取和释放功能，支持可重入锁和锁超时。
 *
 * @author Yakir
 * @since 1.0
 */
@Slf4j
public class LockManage {

	/**
	 * 释放锁的Lua脚本 KEYS[1]: 锁的键名 ARGV[1]: 请求ID，用于防止解除错误的锁
	 */
	private static final DefaultRedisScript<Long> RELEASE_LOCK_LUA_SCRIPT = new DefaultRedisScript<>(
			"if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end",
			Long.class);

	/**
	 * 释放锁成功的返回值
	 */
	private static final Long RELEASE_LOCK_SUCCESS_RESULT = 1L;

	/**
	 * 获取锁，不设置过期时间
	 * @param lockKey 锁的键名
	 * @param requestId 请求ID
	 * @return 是否成功获取锁
	 */
	public static boolean lock(String lockKey, String requestId) {
		return lock(lockKey, requestId, -1L);
	}

	/**
	 * 获取锁，设置过期时间（秒）
	 * @param key 锁的键名
	 * @param requestId 请求ID
	 * @param ttl 过期时间（秒）
	 * @return 是否成功获取锁
	 */
	public static boolean lock(String key, String requestId, Long ttl) {
		return lock(key, requestId, ttl, TimeUnit.SECONDS);
	}

	/**
	 * 获取锁，设置过期时间和时间单位
	 * @param key 锁的键名
	 * @param requestId 请求ID
	 * @param ttl 过期时间
	 * @param timeUnit 时间单位
	 * @return 是否成功获取锁
	 */
	public static boolean lock(String key, String requestId, Long ttl, TimeUnit timeUnit) {
		log.trace("lock: {key:{}, clientId:{}}", key, requestId);
		if (ttl < 0) {
			return RedisHelper.setNx(key, requestId);
		}
		return RedisHelper.setNxEx(key, requestId, ttl, timeUnit);
	}

	/**
	 * 获取锁，设置过期时间和超时时间（秒）
	 * @param key 锁的键名
	 * @param requestId 请求ID
	 * @param ttl 过期时间（秒）
	 * @param timeout 获取锁的超时时间（秒）
	 * @return 是否成功获取锁
	 */
	public static boolean lock(String key, String requestId, Long ttl, long timeout) {
		return lock(key, requestId, ttl, timeout, TimeUnit.SECONDS);
	}

	/**
	 * 获取锁，设置过期时间、超时时间和时间单位
	 * @param key 锁的键名
	 * @param requestId 请求ID
	 * @param ttl 过期时间
	 * @param timeout 获取锁的超时时间
	 * @param timeUnit 时间单位
	 * @return 是否成功获取锁
	 */
	public static boolean lock(String key, String requestId, Long ttl, long timeout, TimeUnit timeUnit) {
		long startTime = System.currentTimeMillis();
		boolean token;
		do {
			token = lock(key, requestId, ttl, timeUnit);
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
	 * @param key 锁的键名
	 * @param requestId 请求ID
	 * @return 是否成功释放锁
	 */
	public static boolean releaseLock(String key, String requestId) {
		log.trace("release lock: {key:{}, clientId:{}}", key, requestId);
		Long result = RedisHelper.execute(RELEASE_LOCK_LUA_SCRIPT, Collections.singletonList(key), requestId);
		return Objects.equals(result, RELEASE_LOCK_SUCCESS_RESULT);
	}

}

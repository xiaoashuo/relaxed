package com.relaxed.common.redis.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Yakir
 * @Topic LockManage
 * @Description
 * @date 2022/10/12 13:50
 * @Version 1.0
 */
@Slf4j
public class LockManage {

	private static StringRedisTemplate redisTemplate;

	public void setStringRedisTemplate(StringRedisTemplate redisTemplate) {
		LockManage.redisTemplate = redisTemplate;
	}

	/**
	 *
	 * 释放锁lua脚本 KEYS【1】：key值是为要加的锁定义的字符串常量 ARGV【1】：value值是 request id, 用来防止解除了不该解除的锁. 可用
	 * UUID
	 */
	private static final DefaultRedisScript<Long> RELEASE_LOCK_LUA_SCRIPT = new DefaultRedisScript<>(
			"if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end",
			Long.class);

	/**
	 * 释放锁成功返回值
	 */
	private static final Long RELEASE_LOCK_SUCCESS_RESULT = 1L;

	/**
	 * 上锁
	 * @date 2022/10/12 13:54
	 * @param lockKey 锁key
	 * @param requestId 请求id
	 * @return boolean
	 */
	public boolean lock(String lockKey, String requestId) {
		return lock(lockKey, requestId, -1L);
	}

	public boolean lock(String key, String requestId, Long ttl) {
		return lock(key, requestId, ttl, TimeUnit.SECONDS);
	}

	public boolean lock(String key, String requestId, Long ttl, TimeUnit timeUnit) {
		log.trace("lock: {key:{}, clientId:{}}", key, requestId);
		if (ttl < 0) {
			return redisTemplate.opsForValue().setIfAbsent(key, requestId);
		}
		return redisTemplate.opsForValue().setIfAbsent(key, requestId, ttl, timeUnit);

	}

	public boolean lock(String key, String requestId, Long ttl, long timeout) {
		return lock(key, requestId, ttl, timeout, TimeUnit.SECONDS);
	}

	public boolean lock(String key, String requestId, Long ttl, long timeout, TimeUnit timeUnit) {
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

	public boolean releaseLock(String key, String requestId) {
		log.trace("release lock: {key:{}, clientId:{}}", key, requestId);
		Long result = redisTemplate.execute(RELEASE_LOCK_LUA_SCRIPT, Collections.singletonList(key), requestId);
		return Objects.equals(result, RELEASE_LOCK_SUCCESS_RESULT);
	}

}

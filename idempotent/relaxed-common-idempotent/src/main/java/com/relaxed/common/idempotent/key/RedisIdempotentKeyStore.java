package com.relaxed.common.idempotent.key;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * Redis 幂等键存储实现类。 使用 {@link StringRedisTemplate} 实现 Redis 中的幂等键存储，主要功能包括： 1. 支持分布式环境 2.
 * 支持自动过期 3. 支持原子操作
 *
 * @author Yakir
 * @since 1.0
 */
@RequiredArgsConstructor
public class RedisIdempotentKeyStore extends AbstractIdempotentKeyStore {

	private final StringRedisTemplate stringRedisTemplate;

	/**
	 * 当不存在有效 key 时将其存储下来。 使用 Redis 的 setIfAbsent 操作确保原子性。
	 * @param key 幂等键
	 * @param duration key 的有效时长
	 * @param timeUnit 时长单位
	 * @return true 表示存储成功，false 表示存储失败
	 */
	@Override
	public boolean saveIfAbsent(String key, long duration, TimeUnit timeUnit) {
		ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
		Boolean saveSuccess = opsForValue.setIfAbsent(key, String.valueOf(System.currentTimeMillis()), duration,
				timeUnit);
		return saveSuccess != null && saveSuccess;
	}

	/**
	 * 删除指定的幂等键。
	 * @param key 幂等键
	 */
	@Override
	public void remove(String key) {
		stringRedisTemplate.delete(key);
	}

}

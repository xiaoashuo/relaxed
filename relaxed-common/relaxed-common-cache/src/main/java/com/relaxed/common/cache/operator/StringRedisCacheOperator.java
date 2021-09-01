package com.relaxed.common.cache.operator;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Yakir
 * @Topic StringRedisCacheOperator
 * @Description
 * @date 2021/9/1 17:14
 * @Version 1.0
 */
public class StringRedisCacheOperator extends AbstractRedisCacheOperator<String> {

	public StringRedisCacheOperator(RedisTemplate<String, String> redisTemplate) {
		super(redisTemplate);
	}

}

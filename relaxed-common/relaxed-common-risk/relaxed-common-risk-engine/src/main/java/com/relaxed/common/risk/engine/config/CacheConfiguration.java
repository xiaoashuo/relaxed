package com.relaxed.common.risk.engine.config;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.relaxed.common.cache.CacheOperator;
import com.relaxed.common.cache.operator.StringMemoryCacheOperator;
import com.relaxed.common.cache.operator.StringRedisCacheOperator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author Yakir
 * @Topic CacheConfiguration
 * @Description 缓存配置
 * @date 2021/9/1 17:52
 * @Version 1.0
 */
@Configuration(proxyBeanMethods = false)
public class CacheConfiguration {

	// /**
	// * 本地缓存 默认使用本地缓存
	// * @author yakir
	// * @date 2021/9/1 17:24
	// * @return com.relaxed.common.cache.CacheOperator
	// */
	// @Bean
	// public CacheOperator<String> stringMemoryCacheOperator() {
	// TimedCache<String, String> stringTimedCache =
	// CacheUtil.newTimedCache(Integer.MAX_VALUE);
	// return new StringMemoryCacheOperator(stringTimedCache);
	// }
	//
	// /**
	// * redis 缓存
	// *
	// * @author yakir
	// * @date 2021/9/1 18:13
	// * @param stringRedisTemplate
	// * @return com.relaxed.common.cache.CacheOperator<java.lang.String>
	// */
	// @Bean
	// @ConditionalOnBean(StringRedisTemplate.class)
	// public CacheOperator<String> redisCacheOperator(StringRedisTemplate
	// stringRedisTemplate) {
	// StringRedisCacheOperator stringRedisCacheOperator = new
	// StringRedisCacheOperator(stringRedisTemplate);
	// return stringRedisCacheOperator;
	// }

}

package com.relaxed.common.cache.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.relaxed.common.cache.CacheManage;
import com.relaxed.common.cache.core.CacheStringAspect;
import com.relaxed.common.cache.generate.KeyGenerator;
import com.relaxed.common.cache.generate.PrefixKeyGenerator;
import com.relaxed.common.cache.lock.LockManage;
import com.relaxed.common.cache.lock.operator.LockOperator;
import com.relaxed.common.cache.lock.operator.RedisLockOperator;
import com.relaxed.common.cache.operator.CacheOperator;
import com.relaxed.common.cache.operator.redis.StringRedisCacheOperator;
import com.relaxed.common.cache.serialize.CacheSerializer;

import com.relaxed.common.cache.serialize.JacksonSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

/**
 * @author Yakir
 * @Topic RedisAutoConfiguration
 * @Description
 * @date 2021/7/23 16:04
 * @Version 1.0
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(CacheProperties.class)
public class CacheAutoConfiguration {

	private final RedisConnectionFactory redisConnectionFactory;

	/**
	 * 初始化配置类
	 * @return GlobalCacheProperties
	 */
	@Bean
	public CachePropertiesHolder cachePropertiesHolder(CacheProperties cacheProperties) {
		CachePropertiesHolder cachePropertiesHolder = new CachePropertiesHolder();
		cachePropertiesHolder.setCacheProperties(cacheProperties);
		return cachePropertiesHolder;
	}

	/**
	 * 默认使用redis cache
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean({ CacheProperties.class, CachePropertiesHolder.class })
	public StringRedisTemplate stringRedisTemplate() {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}

	/**
	 * redis缓存操作者
	 * @author yakir
	 * @date 2022/6/13 11:59
	 * @param stringRedisTemplate
	 * @return com.relaxed.common.cache.operator.CacheOperator<java.lang.String>
	 */
	@Bean
	@ConditionalOnMissingBean
	public CacheOperator<String> stringRedisCacheOperator(StringRedisTemplate stringRedisTemplate) {
		return new StringRedisCacheOperator(stringRedisTemplate);
	}

	/**
	 * 缓存前缀key生成器 前缀key生成器
	 * @author yakir
	 * @date 2022/6/13 11:58
	 * @return com.relaxed.common.cache.generate.KeyGenerator
	 */
	@Bean
	@ConditionalOnMissingBean
	public KeyGenerator prefixKeyGenerator(CacheProperties cacheProperties) {
		return new PrefixKeyGenerator(cacheProperties.getKeyPrefix());
	}

	/**
	 * 缓存管理器
	 * @author yakir
	 * @date 2022/6/13 12:00
	 * @param cacheOperator
	 * @param keyGenerator
	 * @return com.relaxed.common.cache.CacheManage<java.lang.String>
	 */
	@Bean
	public CacheManage<String> cacheManage(CacheOperator cacheOperator, KeyGenerator keyGenerator) {
		return new CacheManage<>(cacheOperator, keyGenerator);
	}

	/******************************** 锁配置 ********************************/

	@Bean
	@ConditionalOnMissingBean
	public LockOperator<String> stringLockOperator(StringRedisTemplate stringRedisTemplate) {
		return new RedisLockOperator<>(stringRedisTemplate);
	}

	@Bean
	public LockManage<String> lockManage(LockOperator lockOperator, KeyGenerator keyGenerator) {
		return new LockManage<>(lockOperator, keyGenerator);
	}

	/**
	 *
	 * @author yakir
	 * @date 2022/6/13 15:26
	 * @param objectMapper
	 * @return com.relaxed.common.cache.serialize.CacheSerializer
	 */
	@Bean
	@ConditionalOnMissingBean
	public CacheSerializer cacheSerializer(ObjectMapper objectMapper) {
		return new JacksonSerializer(objectMapper);
	}

	/**
	 * 缓存注解操作切面</br>
	 * 必须在CacheManage初始化之后使用
	 * @param applicationContext 缓存管理
	 * @param cacheManage 缓存管理
	 * @param lockManage 锁管理
	 * @param cacheSerializer 缓存序列化器
	 * @return CacheStringAspect 缓存注解操作切面
	 */
	@Bean
	public CacheStringAspect cacheStringAspect(ApplicationContext applicationContext, CacheManage cacheManage,
			LockManage lockManage, CacheSerializer cacheSerializer) {
		return new CacheStringAspect(applicationContext, cacheManage, lockManage, cacheSerializer);
	}

}

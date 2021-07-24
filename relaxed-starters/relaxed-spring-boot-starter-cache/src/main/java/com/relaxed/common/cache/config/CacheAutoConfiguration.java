package com.relaxed.common.cache.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.relaxed.common.cache.core.CacheAspect;
import com.relaxed.common.cache.lock.CacheManage;
import com.relaxed.common.cache.lock.RedisCacheManage;
import com.relaxed.common.cache.serialize.CacheSerializer;
import com.relaxed.common.cache.serialize.JacksonSerializer;
import com.relaxed.common.cache.serialize.PrefixStringRedisSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

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
	 * 默认使用 Jackson 序列化 值序列化器
	 * @param objectMapper objectMapper
	 * @return JacksonSerializer
	 */
	@Bean
	@ConditionalOnMissingBean
	public CacheSerializer cacheSerializer(ObjectMapper objectMapper) {
		return new JacksonSerializer(objectMapper);
	}

	/**
	 * 默认使用redis cache
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public StringRedisTemplate stringRedisTemplate() {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new PrefixStringRedisSerializer(CachePropertiesHolder.keyPrefix()));
		return template;
	}

	/**
	 * 初始化CacheLock
	 * @param stringRedisTemplate 默认使用字符串类型操作，后续扩展
	 * @return CacheLock
	 */
	@Bean
	@ConditionalOnMissingBean
	public CacheManage cacheLock(StringRedisTemplate stringRedisTemplate) {
		RedisCacheManage redisCacheLock = new RedisCacheManage();
		redisCacheLock.setStringRedisTemplate(stringRedisTemplate);
		return redisCacheLock;
	}

	/**
	 * 缓存注解操作切面</br>
	 * 必须在CacheManage初始化之后使用
	 * @param applicationContext 缓存管理
	 * @param cacheManage 缓存管理
	 * @param cacheSerializer 缓存序列化器
	 * @return CacheStringAspect 缓存注解操作切面
	 */
	@Bean
	public CacheAspect cacheStringAspect(ApplicationContext applicationContext, CacheManage cacheManage,
			CacheSerializer cacheSerializer) {
		return new CacheAspect(applicationContext, cacheManage, cacheSerializer);
	}

}

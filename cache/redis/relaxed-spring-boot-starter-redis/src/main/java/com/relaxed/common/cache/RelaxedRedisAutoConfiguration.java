package com.relaxed.common.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.relaxed.common.redis.RedisHelper;
import com.relaxed.common.redis.config.CacheProperties;
import com.relaxed.common.redis.config.CachePropertiesHolder;
import com.relaxed.common.redis.core.CacheStringAspect;
import com.relaxed.common.redis.lock.scheduled.LockRenewalScheduledTask;
import com.relaxed.common.redis.prefix.DefaultRedisPrefixConverter;
import com.relaxed.common.redis.prefix.IRedisPrefixConverter;
import com.relaxed.common.redis.serialize.RelaxedRedisSerializer;
import com.relaxed.common.redis.serialize.JacksonSerializerRelaxed;
import com.relaxed.common.redis.serialize.PrefixJdkRedisSerializer;
import com.relaxed.common.redis.serialize.PrefixStringRedisSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author Yakir
 * @Topic RedisAutoConfiguraion
 * @Description
 * @date 2022/10/12 11:24
 * @Version 1.0
 */
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(CacheProperties.class)
public class RelaxedRedisAutoConfiguration {

	private final RedisConnectionFactory redisConnectionFactory;

	/**
	 * 初始化配置类
	 * @return GlobalCacheProperties
	 */
	@Bean
	@ConditionalOnMissingBean
	public CachePropertiesHolder cachePropertiesHolder(CacheProperties cacheProperties) {
		CachePropertiesHolder cachePropertiesHolder = new CachePropertiesHolder();
		cachePropertiesHolder.setCacheProperties(cacheProperties);
		return cachePropertiesHolder;
	}

	/**
	 * 默认使用 Jackson 序列化
	 * @param objectMapper objectMapper
	 * @return JacksonSerializer
	 */
	@Bean
	@ConditionalOnMissingBean
	public RelaxedRedisSerializer relaxedRedisSerializer(ObjectMapper objectMapper) {
		return new JacksonSerializerRelaxed(objectMapper);
	}

	/**
	 * 缓存注解操作切面</br>
	 * 必须在CacheLock初始化之后使用
	 * @param stringRedisTemplate 字符串存储的Redis操作类
	 * @param relaxedRedisSerializer 缓存序列化器
	 * @return CacheStringAspect 缓存注解操作切面
	 */
	@Bean
	@ConditionalOnMissingBean
	public CacheStringAspect cacheStringAspect(StringRedisTemplate stringRedisTemplate,
			RelaxedRedisSerializer relaxedRedisSerializer) {
		return new CacheStringAspect(stringRedisTemplate, relaxedRedisSerializer);
	}

	/**
	 * redis key 前缀处理器
	 * @return IRedisPrefixConverter
	 */
	@Bean
	@DependsOn("cachePropertiesHolder")
	@ConditionalOnProperty(prefix = "relaxed.redis", name = "key-prefix")
	@ConditionalOnMissingBean(IRedisPrefixConverter.class)
	public IRedisPrefixConverter redisPrefixConverter() {
		return new DefaultRedisPrefixConverter(CachePropertiesHolder.keyPrefix());
	}

	/**
	 * 锁续期
	 * @date 2022/10/12 19:50
	 * @return com.relaxed.common.redis.lock.scheduled.LockRenewalScheduledTask
	 */
	@Bean
	@ConditionalOnProperty(prefix = "relaxed.redis", name = "lockRenewal", havingValue = "true")
	@ConditionalOnMissingBean
	public LockRenewalScheduledTask lockRenewalScheduledTask() {
		LockRenewalScheduledTask lockRenewalScheduledTask = new LockRenewalScheduledTask();
		return lockRenewalScheduledTask;
	}

	@Bean
	@ConditionalOnBean(IRedisPrefixConverter.class)
	@ConditionalOnMissingBean
	public StringRedisTemplate stringRedisTemplate(IRedisPrefixConverter redisPrefixConverter) {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new PrefixStringRedisSerializer(redisPrefixConverter));
		return template;
	}

	@Bean
	@ConditionalOnBean(IRedisPrefixConverter.class)
	@ConditionalOnMissingBean(name = "redisTemplate")
	public RedisTemplate<Object, Object> redisTemplate(IRedisPrefixConverter redisPrefixConverter) {
		RedisTemplate<Object, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new PrefixJdkRedisSerializer(redisPrefixConverter));
		return template;
	}

	@Bean
	@ConditionalOnMissingBean(RedisHelper.class)
	public RedisHelper redisHelper(StringRedisTemplate template) {
		RedisHelper.setRedisTemplate(template);
		return RedisHelper.INSTANCE;
	}

}

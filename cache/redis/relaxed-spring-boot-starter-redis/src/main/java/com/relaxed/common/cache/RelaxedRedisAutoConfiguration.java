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
 * Redis自动配置类，提供Redis相关的自动配置功能。 包括Redis序列化器、缓存注解切面、键前缀处理器、锁续期任务等组件的配置。
 *
 * @author Yakir
 * @since 1.0
 */
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(CacheProperties.class)
public class RelaxedRedisAutoConfiguration {

	private final RedisConnectionFactory redisConnectionFactory;

	/**
	 * 初始化缓存配置持有者
	 * @param cacheProperties 缓存配置属性
	 * @return 缓存配置持有者实例
	 */
	@Bean
	@ConditionalOnMissingBean
	public CachePropertiesHolder cachePropertiesHolder(CacheProperties cacheProperties) {
		CachePropertiesHolder cachePropertiesHolder = new CachePropertiesHolder();
		cachePropertiesHolder.setCacheProperties(cacheProperties);
		return cachePropertiesHolder;
	}

	/**
	 * 配置默认的Redis序列化器，使用Jackson进行序列化
	 * @param objectMapper Jackson对象映射器
	 * @return Redis序列化器实例
	 */
	@Bean
	@ConditionalOnMissingBean
	public RelaxedRedisSerializer relaxedRedisSerializer(ObjectMapper objectMapper) {
		return new JacksonSerializerRelaxed(objectMapper);
	}

	/**
	 * 配置缓存注解操作切面 注意：必须在CacheLock初始化之后使用
	 * @param stringRedisTemplate 字符串存储的Redis操作类
	 * @param relaxedRedisSerializer 缓存序列化器
	 * @return 缓存注解操作切面实例
	 */
	@Bean
	@ConditionalOnMissingBean
	public CacheStringAspect cacheStringAspect(StringRedisTemplate stringRedisTemplate,
			RelaxedRedisSerializer relaxedRedisSerializer) {
		return new CacheStringAspect(stringRedisTemplate, relaxedRedisSerializer);
	}

	/**
	 * 配置Redis键前缀处理器
	 * @return Redis键前缀转换器实例
	 */
	@Bean
	@DependsOn("cachePropertiesHolder")
	@ConditionalOnProperty(prefix = "relaxed.redis", name = "key-prefix")
	@ConditionalOnMissingBean(IRedisPrefixConverter.class)
	public IRedisPrefixConverter redisPrefixConverter() {
		return new DefaultRedisPrefixConverter(CachePropertiesHolder.keyPrefix());
	}

	/**
	 * 配置分布式锁续期任务
	 * @return 锁续期任务实例
	 */
	@Bean
	@ConditionalOnProperty(prefix = "relaxed.redis", name = "lockRenewal", havingValue = "true")
	@ConditionalOnMissingBean
	public LockRenewalScheduledTask lockRenewalScheduledTask() {
		return new LockRenewalScheduledTask();
	}

	/**
	 * 配置带前缀的字符串Redis模板
	 * @param redisPrefixConverter Redis键前缀转换器
	 * @return 字符串Redis模板实例
	 */
	@Bean
	@ConditionalOnBean(IRedisPrefixConverter.class)
	@ConditionalOnMissingBean
	public StringRedisTemplate stringRedisTemplate(IRedisPrefixConverter redisPrefixConverter) {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new PrefixStringRedisSerializer(redisPrefixConverter));
		return template;
	}

	/**
	 * 配置带前缀的Redis模板
	 * @param redisPrefixConverter Redis键前缀转换器
	 * @return Redis模板实例
	 */
	@Bean
	@ConditionalOnBean(IRedisPrefixConverter.class)
	@ConditionalOnMissingBean(name = "redisTemplate")
	public RedisTemplate<Object, Object> redisTemplate(IRedisPrefixConverter redisPrefixConverter) {
		RedisTemplate<Object, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new PrefixJdkRedisSerializer(redisPrefixConverter));
		return template;
	}

	/**
	 * 配置Redis操作辅助类
	 * @param template 字符串Redis模板
	 * @return Redis操作辅助类实例
	 */
	@Bean
	@ConditionalOnMissingBean(RedisHelper.class)
	public RedisHelper redisHelper(StringRedisTemplate template) {
		RedisHelper.setRedisTemplate(template);
		return RedisHelper.INSTANCE;
	}

}

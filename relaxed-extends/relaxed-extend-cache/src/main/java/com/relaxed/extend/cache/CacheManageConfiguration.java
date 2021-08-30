package com.relaxed.extend.cache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Yakir
 * @Topic CacheManageConfiguration
 * @Description
 * @date 2021/8/27 9:51
 * @Version 1.0
 */
@Configuration(proxyBeanMethods = false)
public class CacheManageConfiguration {


    /**
     * 注册缓存管理服务
     * @author yakir
     * @date 2021/8/27 9:52
     * @return com.relaxed.extend.cache.CacheManage
     */
    @Bean
    @ConditionalOnMissingBean
    public CacheManage cacheManage(){
        return new RedisCacheManage();
    }
}

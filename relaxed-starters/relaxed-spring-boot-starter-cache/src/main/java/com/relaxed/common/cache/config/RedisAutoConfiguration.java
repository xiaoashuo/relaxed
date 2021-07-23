package com.relaxed.common.cache.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author Yakir
 * @Topic RedisAutoConfiguration
 * @Description
 * @date 2021/7/23 16:04
 * @Version 1.0
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(CacheProperties.class)
public class RedisAutoConfiguration {
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
}

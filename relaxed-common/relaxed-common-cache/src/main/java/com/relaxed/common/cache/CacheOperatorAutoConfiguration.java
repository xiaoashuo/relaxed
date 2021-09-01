package com.relaxed.common.cache;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.relaxed.common.cache.operator.StringMemoryCacheOperator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yakir
 * @Topic CacheOperatorConfiguration
 * @Description
 * @date 2021/9/1 17:15
 * @Version 1.0
 */
@Configuration(proxyBeanMethods = false)
public class CacheOperatorAutoConfiguration {

	/**
	 * 本地缓存 默认使用本地缓存
	 * @author yakir
	 * @date 2021/9/1 17:24
	 * @return com.relaxed.common.cache.CacheOperator
	 */
	@Bean
	@ConditionalOnMissingBean
	public CacheOperator stringMemoryCacheOperator() {
		TimedCache<String, String> stringTimedCache = CacheUtil.newTimedCache(Integer.MAX_VALUE);
		return new StringMemoryCacheOperator(stringTimedCache);
	}

}

package com.relaxed.common.risk.engine.config;

import com.relaxed.common.risk.engine.cache.CacheService;
import com.relaxed.common.risk.engine.cache.LocalCache;
import com.relaxed.common.risk.engine.core.distributor.EventDistributor;
import com.relaxed.common.risk.engine.core.distributor.RedisDistributor;
import com.relaxed.common.risk.engine.core.handler.DefaultFieldValidateHandler;
import com.relaxed.common.risk.engine.core.handler.DefaultRiskReportHandler;
import com.relaxed.common.risk.engine.core.handler.FieldValidateHandler;
import com.relaxed.common.risk.engine.core.handler.RiskReportHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author Yakir
 * @Topic RiskEngineConfiguration
 * @Description
 * @date 2021/8/29 10:05
 * @Version 1.0
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(value = { EngineProperties.class })
public class RiskEngineConfiguration {

	/**
	 * 缓存服务
	 * @author yakir
	 * @date 2021/8/29 10:06
	 * @return com.relaxed.common.risk.engine.cache.CacheService
	 */
	@Bean
	@ConditionalOnMissingBean
	public CacheService cacheService() {
		return new LocalCache();
	}

	/**
	 * 事件分发者
	 * @author yakir
	 * @date 2021/8/29 10:09
	 * @param stringRedisTemplate
	 * @return com.relaxed.common.risk.engine.core.distributor.EventDistributor
	 */
	@Bean
	@ConditionalOnMissingBean
	public EventDistributor eventDistributor(StringRedisTemplate stringRedisTemplate) {
		return new RedisDistributor(stringRedisTemplate);
	}

	/**
	 * 字段验证处理器
	 * @author yakir
	 * @date 2021/8/29 13:48
	 * @return com.relaxed.common.risk.engine.core.handler.FieldValidateHandler
	 */
	@Bean
	@ConditionalOnMissingBean
	public FieldValidateHandler fieldValidateHandler() {
		return new DefaultFieldValidateHandler();
	}

	/**
	 * 风控评估报告处理器
	 * @author yakir
	 * @date 2021/8/31 15:23
	 * @return com.relaxed.common.risk.engine.core.handler.RiskReportHandler
	 */
	@Bean
	@ConditionalOnMissingBean
	public RiskReportHandler riskReportHandler() {
		return new DefaultRiskReportHandler();
	}

}

package com.relaxed.common.risk.engine.config;

import com.relaxed.common.risk.engine.cache.CacheService;
import com.relaxed.common.risk.engine.cache.LocalCache;
import com.relaxed.common.risk.engine.core.handler.DefaultFieldValidateHandler;
import com.relaxed.common.risk.engine.core.handler.DefaultRiskReportHandler;
import com.relaxed.common.risk.engine.core.handler.FieldValidateHandler;
import com.relaxed.common.risk.engine.core.handler.RiskReportHandler;
import com.relaxed.common.risk.engine.mongdb.DefaultMongoDbServiceImpl;
import com.relaxed.common.risk.engine.mongdb.MongoDbService;
import com.relaxed.common.risk.engine.service.ModelEventManageService;
import com.relaxed.common.risk.engine.service.impl.ModelEventManageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

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
	 * 模型事件处理器
	 * @author yakir
	 * @date 2021/9/23 11:41
	 * @param mongoTemplate
	 * @return com.relaxed.common.risk.engine.service.ModelEventManageService
	 */
	@SuppressWarnings({ "all" })
	@Bean
	@ConditionalOnMissingBean
	public ModelEventManageService modelEventManageService(MongoTemplate mongoTemplate) {
		DefaultMongoDbServiceImpl defaultMongoDbService = new DefaultMongoDbServiceImpl(mongoTemplate);
		return new ModelEventManageServiceImpl(defaultMongoDbService);
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

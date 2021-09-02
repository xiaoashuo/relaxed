package com.relaxed.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yakir
 * @Topic ConfigAutoConfiguration
 * @Description
 * @date 2021/9/2 10:18
 * @Version 1.0
 */
@Configuration(proxyBeanMethods = false)
public class ConfigAutoConfiguration {

	/**
	 * 配置服务
	 * @author yakir
	 * @date 2021/9/2 10:19
	 * @return com.relaxed.common.config.Config<java.lang.String,java.lang.Object>
	 */
	@Bean
	@ConditionalOnMissingBean
	public Config<String, Object> config() {
		return new LocalMemoryConfig();
	}

}

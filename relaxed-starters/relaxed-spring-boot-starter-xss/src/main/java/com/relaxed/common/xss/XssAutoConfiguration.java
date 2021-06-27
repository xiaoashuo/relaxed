package com.relaxed.common.xss;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.relaxed.common.xss.config.XssProperties;
import com.relaxed.common.xss.filters.XssFilter;
import com.relaxed.common.xss.json.XssStringJsonDeserializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yakir
 * @Topic WafConfiguration
 * @Description
 * @date 2021/6/26 14:21
 * @Version 1.0
 */
@RequiredArgsConstructor
@Slf4j
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties({ XssProperties.class })
@ConditionalOnProperty(prefix = XssProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class XssAutoConfiguration {

	private final XssProperties xssProperties;

	/**
	 * 主要用于过滤 QueryString, Header 以及 form 中的参数
	 * @return FilterRegistrationBean
	 */
	@Bean
	public FilterRegistrationBean<XssFilter> xssFilterRegistrationBean() {
		log.debug("XSS 过滤已开启====");
		XssFilter xssFilter = new XssFilter(xssProperties);
		FilterRegistrationBean<XssFilter> registrationBean = new FilterRegistrationBean<>(xssFilter);
		registrationBean.setOrder(-1);
		return registrationBean;
	}

	/**
	 * 注册 Jackson 的序列化器，用于处理 json 类型参数的 xss 过滤
	 * @return Jackson2ObjectMapperBuilderCustomizer
	 */
	@Bean
	@ConditionalOnMissingBean(name = "xssJacksonCustomizer")
	@ConditionalOnBean(ObjectMapper.class)
	public Jackson2ObjectMapperBuilderCustomizer xssJacksonCustomizer() {
		// 在反序列化时进行 xss 过滤，可以替换使用 XssStringJsonSerializer，在序列化时进行处理
		return builder -> builder.deserializerByType(String.class, new XssStringJsonDeserializer(xssProperties));
	}

}

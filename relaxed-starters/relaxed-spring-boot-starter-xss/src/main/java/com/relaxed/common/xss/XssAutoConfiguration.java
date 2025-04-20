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
 * XSS自动配置类 配置XSS过滤相关的组件，包括过滤器、Jackson序列化器等 支持通过配置文件控制XSS过滤的开启和关闭
 *
 * @author Yakir
 * @since 1.0
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
	 * 注册XSS过滤器 主要用于过滤QueryString、Header以及form表单中的参数
	 * @return XSS过滤器注册Bean
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
	 * 注册Jackson的XSS序列化器 用于处理JSON类型参数的XSS过滤 在反序列化时进行XSS过滤
	 * @return Jackson序列化器自定义器
	 */
	@Bean
	@ConditionalOnMissingBean(name = "xssJacksonCustomizer")
	@ConditionalOnBean(ObjectMapper.class)
	public Jackson2ObjectMapperBuilderCustomizer xssJacksonCustomizer() {
		return builder -> builder.deserializerByType(String.class, new XssStringJsonDeserializer(xssProperties));
	}

}

package com.relaxed.autoconfigure.web.servlet;

import com.relaxed.autoconfigure.web.config.RelaxedWebProperties;
import com.relaxed.autoconfigure.web.filter.TraceIdFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Web MVC自动配置类 配置Web MVC相关的组件，包括分页参数解析器和TraceId过滤器
 *
 * @author Yakir
 * @since 1.0
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(RelaxedWebProperties.class)
@Configuration
public class WebMvcAutoConfiguration {

	private final RelaxedWebProperties webProperties;

	/**
	 * 创建分页参数解析器 用于解析Controller方法中的分页参数
	 * @return 分页参数解析器
	 */
	@Bean
	@ConditionalOnMissingBean
	public PageParamArgumentResolver pageParamArgumentResolver() {
		return new PageParamArgumentResolver(webProperties.getPageSizeLimit());
	}

	/**
	 * 自定义Web MVC配置 配置方法参数解析器
	 */
	@RequiredArgsConstructor
	@Configuration(proxyBeanMethods = false)
	static class CustomWebMvcConfigurer implements WebMvcConfigurer {

		private final PageParamArgumentResolver pageParamArgumentResolver;

		/**
		 * 添加分页参数解析器 用于解析分页查询参数，防止SQL注入
		 * @param argumentResolvers 方法参数解析器集合
		 */
		@Override
		public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
			argumentResolvers.add(pageParamArgumentResolver);
		}

	}

	/**
	 * 创建TraceId过滤器 用于在请求中添加TraceId，方便日志追踪
	 * @return TraceId过滤器注册Bean
	 */
	@Bean
	public FilterRegistrationBean<TraceIdFilter> traceIdFilterRegistrationBean() {
		FilterRegistrationBean<TraceIdFilter> registrationBean = new FilterRegistrationBean<>(new TraceIdFilter());
		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return registrationBean;
	}

}

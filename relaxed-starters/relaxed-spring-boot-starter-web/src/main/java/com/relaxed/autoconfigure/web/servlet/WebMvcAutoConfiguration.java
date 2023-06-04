package com.relaxed.autoconfigure.web.servlet;

import com.relaxed.autoconfigure.web.filter.TraceIdFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * web mvc config
 *
 * @author Yakir
 * @since 2021/3/19
 */
@Configuration
public class WebMvcAutoConfiguration {

	@Value("${relaxed.web.page-size-limit:100}")
	private int pageSizeLimit;

	@Bean
	@ConditionalOnMissingBean
	public PageParamArgumentResolver pageParamArgumentResolver() {
		return new PageParamArgumentResolver(pageSizeLimit);
	}

	@RequiredArgsConstructor
	@Configuration(proxyBeanMethods = false)
	static class CustomWebMvcConfigurer implements WebMvcConfigurer {

		private final PageParamArgumentResolver pageParamArgumentResolver;

		/**
		 * Page Sql注入过滤
		 * @param argumentResolvers 方法参数解析器集合
		 */
		@Override
		public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
			argumentResolvers.add(pageParamArgumentResolver);
		}

	}

	/**
	 * trace id 过滤器
	 * @return
	 */
	@Bean
	public FilterRegistrationBean<TraceIdFilter> traceIdFilterRegistrationBean() {
		FilterRegistrationBean<TraceIdFilter> registrationBean = new FilterRegistrationBean<>(new TraceIdFilter());
		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return registrationBean;
	}

}

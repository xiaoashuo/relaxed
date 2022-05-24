package com.relaxed.common.swagger;

import com.relaxed.common.swagger.property.SwaggerProperties;
import com.relaxed.common.swagger.property.SwaggerProviderProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yakir
 * @Topic SwaggerProviderAutoConfiguration
 * @Description
 * @date 2021/7/8 13:58
 * @Version 1.0
 */
@Import(SwaggerAutoConfiguration.class)
@ConditionalOnProperty(prefix = SwaggerProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerProviderAutoConfiguration {

	private static final String ALL = "*";

	@Bean
	@ConditionalOnMissingBean
	public SwaggerProviderProperties swaggerProviderProperties() {
		return new SwaggerProviderProperties();
	}

	/**
	 * 允许swagger文档跨域访问 解决聚合文档导致的跨域问题
	 * @return FilterRegistrationBean
	 */
	@Bean
	@ConditionalOnBean(SwaggerProviderProperties.class)
	public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean(
			SwaggerProviderProperties swaggerProviderProperties) {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		String aggregatorOrigin = swaggerProviderProperties.getAggregatorOrigin();
		config.setAllowCredentials(true);
		// 在 springmvc 5.3 版本之后，跨域来源使用 * 号进行匹配的方式进行调整
		if (ALL.equals(aggregatorOrigin)) {
			config.addAllowedOriginPattern(ALL);
		}
		else {
			config.addAllowedOrigin(aggregatorOrigin);
		}
		config.addAllowedHeader(ALL);
		config.addAllowedMethod(ALL);
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}

}

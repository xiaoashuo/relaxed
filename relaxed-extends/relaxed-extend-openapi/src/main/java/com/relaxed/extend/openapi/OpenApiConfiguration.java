package com.relaxed.extend.openapi;

import com.relaxed.common.model.domain.PageParam;
import com.relaxed.common.model.domain.PageParamRequest;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.SpringDocUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * OpenAPI自动配置类。 用于配置Swagger/OpenAPI文档的基本信息，包括文档标题、描述、版本、联系人等。
 *
 * @author hccake
 * @since 1.0
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(OpenApiProperties.class)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = OpenApiProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class OpenApiConfiguration {

	private final OpenApiProperties openApiProperties;

	static {
		// 由于PageParam是由自定义的PageParamArgumentResolver处理的，所以需要在文档上进行入参的格式转换
		SpringDocUtils config = SpringDocUtils.getConfig();
		config.replaceParameterObjectWithClass(PageParam.class, PageParamRequest.class);
	}

	/**
	 * 创建OpenAPI配置
	 * @return OpenAPI配置对象
	 */
	@Bean
	@ConditionalOnMissingBean(OpenAPI.class)
	@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
	public OpenAPI openAPI() {
		OpenAPI openAPI = new OpenAPI();

		// 文档基本信息
		OpenApiProperties.InfoProperties infoProperties = openApiProperties.getInfo();
		Info info = convertInfo(infoProperties);
		openAPI.info(info);

		// 扩展文档信息
		openAPI.externalDocs(openApiProperties.getExternalDocs());
		openAPI.servers(openApiProperties.getServers());
		openAPI.security(openApiProperties.getSecurity());
		openAPI.tags(openApiProperties.getTags());
		openAPI.paths(openApiProperties.getPaths());
		openAPI.components(openApiProperties.getComponents());
		openAPI.extensions(openApiProperties.getExtensions());

		return openAPI;
	}

	/**
	 * 转换InfoProperties为Info对象
	 * @param infoProperties InfoProperties对象
	 * @return Info对象
	 */
	@SuppressWarnings("java:S1854")
	private Info convertInfo(OpenApiProperties.InfoProperties infoProperties) {
		Info info = new Info();
		info.setTitle(infoProperties.getTitle());
		info.setDescription(infoProperties.getDescription());
		info.setTermsOfService(infoProperties.getTermsOfService());
		info.setContact(infoProperties.getContact());
		info.setLicense(infoProperties.getLicense());
		info.setVersion(infoProperties.getVersion());
		info.setExtensions(infoProperties.getExtensions());
		return info;
	}

	/**
	 * 配置CORS过滤器，允许聚合者对提供者的文档进行跨域访问
	 * @return CORS过滤器注册Bean
	 */
	@Bean
	@ConditionalOnProperty(prefix = OpenApiProperties.PREFIX + ".cors-config", name = "enabled", havingValue = "true")
	public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
		// 获取CORS配置
		OpenApiProperties.CorsConfig corsConfig = openApiProperties.getCorsConfig();

		// 转换CORS配置
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowedOrigins(corsConfig.getAllowedOrigins());
		corsConfiguration.setAllowedOriginPatterns(corsConfig.getAllowedOriginPatterns());
		corsConfiguration.setAllowedMethods(corsConfig.getAllowedMethods());
		corsConfiguration.setAllowedHeaders(corsConfig.getAllowedHeaders());
		corsConfiguration.setExposedHeaders(corsConfig.getExposedHeaders());
		corsConfiguration.setAllowCredentials(corsConfig.getAllowCredentials());
		corsConfiguration.setMaxAge(corsConfig.getMaxAge());

		// 注册CORS配置与资源的映射关系
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration(corsConfig.getUrlPattern(), corsConfiguration);

		// 注册CORS过滤器，设置最高优先级
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);

		return bean;
	}

}

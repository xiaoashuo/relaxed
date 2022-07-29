package com.relaxed.oauth2.resource.configuration;

import com.relaxed.oauth2.common.handler.CustomAuthenticationEntryPoint;
import com.relaxed.oauth2.resource.CustomPermissionEvaluator;
import com.relaxed.oauth2.resource.services.CustomRemoteTokenServices;
import com.relaxed.oauth2.common.handler.CustomAccessDeniedHandler;

import com.relaxed.oauth2.resource.properties.ExtendResourceServerProperties;
import com.relaxed.oauth2.resource.services.CustomUserInfoTokenServices;
import com.relaxed.oauth2.resource.services.ResourceServiceTokenProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.security.oauth2.resource.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.StringUtils;

/**
 * @author Yakir
 * @Topic ResourceAutoConfiguration
 * @Description
 * @date 2022/7/25 14:55
 * @Version 1.0
 */
@Import(CustomResourceServerConfigurer.class)
@EnableConfigurationProperties({ ExtendResourceServerProperties.class })
public class ResourceAutoConfiguration {

	/**
	 * 自定义的权限判断组件
	 * @return CustomPermissionEvaluator
	 */
	@Bean(name = "per")
	@ConditionalOnMissingBean(CustomPermissionEvaluator.class)
	public CustomPermissionEvaluator customPermissionEvaluator() {
		return new CustomPermissionEvaluator();
	}

	/**
	 * 认证异常处理
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new CustomAuthenticationEntryPoint();
	}

	/**
	 * 进入异常处理
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public AccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}

	/**
	 * 自定义token services 统一异常错误信息提取
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(name = "security.oauth2.resource.token-info-uri")
	public ResourceServiceTokenProvider remoteTokenServiceProvider() {
		return resource -> {
			CustomRemoteTokenServices services = new CustomRemoteTokenServices();
			services.setCheckTokenEndpointUrl(resource.getTokenInfoUri());
			services.setClientId(resource.getClientId());
			services.setClientSecret(resource.getClientSecret());
			return services;
		};

	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(name = "security.oauth2.resource.user-info-uri")
	public ResourceServiceTokenProvider userInfoTokenServicesProvider(
			@Autowired(required = false) UserInfoRestTemplateFactory restTemplateFactory,
			ObjectProvider<AuthoritiesExtractor> authoritiesExtractorProvider,
			ObjectProvider<PrincipalExtractor> principalExtractorProvider) {
		return resource -> {
			CustomUserInfoTokenServices services = new CustomUserInfoTokenServices(resource.getUserInfoUri(),
					resource.getClientId());
			services.setTokenType(resource.getTokenType());
			services.setRestTemplate(restTemplateFactory.getUserInfoRestTemplate());
			AuthoritiesExtractor authoritiesExtractor = authoritiesExtractorProvider.getIfAvailable();
			if (authoritiesExtractor != null) {
				services.setAuthoritiesExtractor(authoritiesExtractor);
			}
			PrincipalExtractor principalExtractor = principalExtractorProvider.getIfAvailable();
			if (principalExtractor != null) {
				services.setPrincipalExtractor(principalExtractor);
			}

			return services;

		};

	}

}

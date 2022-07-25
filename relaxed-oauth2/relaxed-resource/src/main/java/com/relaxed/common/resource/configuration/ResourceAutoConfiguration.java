package com.relaxed.common.resource.configuration;

import com.relaxed.common.resource.CustomPermissionEvaluator;
import com.relaxed.common.resource.CustomRemoteTokenServices;
import com.relaxed.common.resource.RemoteTokenServiceProvider;
import com.relaxed.common.resource.handler.CustomAccessDeniedHandler;
import com.relaxed.common.resource.handler.CustomAuthenticationEntryPoint;
import com.relaxed.common.resource.properties.ExtendResourceServerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

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
	public RemoteTokenServiceProvider remoteTokenServiceProvider() {
		return resource -> {
			CustomRemoteTokenServices services = new CustomRemoteTokenServices();
			services.setCheckTokenEndpointUrl(resource.getTokenInfoUri());
			services.setClientId(resource.getClientId());
			services.setClientSecret(resource.getClientSecret());
			return services;
		};
	}

}

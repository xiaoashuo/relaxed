package com.relaxed.oauth2.resource.configuration;

import com.relaxed.oauth2.common.handler.CustomAuthenticationEntryPoint;
import com.relaxed.oauth2.resource.CustomPermissionEvaluator;
import com.relaxed.oauth2.resource.CustomRemoteTokenServices;
import com.relaxed.oauth2.resource.RemoteTokenServiceProvider;
import com.relaxed.oauth2.common.handler.CustomAccessDeniedHandler;

import com.relaxed.oauth2.resource.properties.ExtendResourceServerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerTokenServicesConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
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
	@Conditional({ ResourceAutoConfiguration.TokenInfoCondition.class })
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

	private static class TokenInfoCondition extends SpringBootCondition {

		private TokenInfoCondition() {
		}

		@Override
		public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
			ConditionMessage.Builder message = ConditionMessage.forCondition("OAuth TokenInfo Condition",
					new Object[0]);
			Environment environment = context.getEnvironment();
			Boolean preferTokenInfo = (Boolean) environment.getProperty("security.oauth2.resource.prefer-token-info",
					Boolean.class);
			if (preferTokenInfo == null) {
				preferTokenInfo = environment.resolvePlaceholders("${OAUTH2_RESOURCE_PREFERTOKENINFO:true}")
						.equals("true");
			}

			String tokenInfoUri = environment.getProperty("security.oauth2.resource.token-info-uri");
			String userInfoUri = environment.getProperty("security.oauth2.resource.user-info-uri");
			if (!StringUtils.hasLength(userInfoUri) && !StringUtils.hasLength(tokenInfoUri)) {
				return ConditionOutcome.noMatch(message.didNotFind("user-info-uri property").atAll());
			}
			else {
				return StringUtils.hasLength(tokenInfoUri) && preferTokenInfo
						? ConditionOutcome.match(message.foundExactly("preferred token-info-uri property"))
						: ConditionOutcome.noMatch(message.didNotFind("token info").atAll());
			}
		}

	}

}

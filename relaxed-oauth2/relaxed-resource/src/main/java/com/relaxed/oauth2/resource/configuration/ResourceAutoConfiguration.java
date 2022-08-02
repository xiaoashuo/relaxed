package com.relaxed.oauth2.resource.configuration;

import cn.hutool.core.util.BooleanUtil;
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
	 * 自定义token services prefer-token-info默认值为true，既优先使用token-info-uri校验token认证信息
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	@Conditional(TokenInfoCondition.class)
	public ResourceServiceTokenProvider remoteTokenServiceProvider() {
		return resource -> {
			CustomRemoteTokenServices services = new CustomRemoteTokenServices();
			services.setCheckTokenEndpointUrl(resource.getTokenInfoUri());
			services.setClientId(resource.getClientId());
			services.setClientSecret(resource.getClientSecret());
			return services;
		};

	}

	/**
	 * prefer-token-info设置为false，或不配置token-info-uri则会使用user-info-uri，适用于需要获取userdetails信息的场景
	 * @param restTemplateFactory
	 * @param authoritiesExtractorProvider
	 * @param principalExtractorProvider
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	@Conditional(UserInfoCondition.class)
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

	private static class UserInfoCondition extends SpringBootCondition {

		private UserInfoCondition() {
		}

		@Override
		public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
			ConditionMessage.Builder message = ConditionMessage.forCondition("OAuth UserInfo Condition", new Object[0]);
			Environment environment = context.getEnvironment();
			Boolean preferTokenInfo = (Boolean) environment.getProperty("security.oauth2.resource.prefer-token-info",
					Boolean.class);
			if (preferTokenInfo == null) {
				preferTokenInfo = environment.resolvePlaceholders("${OAUTH2_RESOURCE_PREFERTOKENINFO:true}")
						.equals("true");
			}
			String userInfoUri = environment.getProperty("security.oauth2.resource.user-info-uri");
			if (BooleanUtil.isFalse(preferTokenInfo) && StringUtils.hasLength(userInfoUri)) {
				return ConditionOutcome.match(message.foundExactly("preferred user-info-uri property"));
			}
			else {
				return ConditionOutcome.noMatch(message.didNotFind("user info").atAll());
			}
		}

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
			if (BooleanUtil.isTrue(preferTokenInfo) && StringUtils.hasLength(tokenInfoUri)) {
				return ConditionOutcome.match(message.foundExactly("preferred token-info-uri property"));
			}
			else {
				return ConditionOutcome.noMatch(message.didNotFind("token info").atAll());
			}
		}

	}

}

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
 * 资源服务器自动配置类 用于自动配置OAuth2资源服务器的相关组件 包括权限评估器、认证入口点、访问拒绝处理器和Token服务
 *
 * @author Yakir
 * @since 1.0
 */
@Import(CustomResourceServerConfigurer.class)
@EnableConfigurationProperties({ ExtendResourceServerProperties.class })
public class ResourceAutoConfiguration {

	/**
	 * 自定义权限评估器 用于评估用户是否具有特定权限 支持通配符匹配权限字符串
	 * @return 自定义权限评估器实例
	 */
	@Bean(name = "per")
	@ConditionalOnMissingBean(CustomPermissionEvaluator.class)
	public CustomPermissionEvaluator customPermissionEvaluator() {
		return new CustomPermissionEvaluator();
	}

	/**
	 * 认证入口点 用于处理认证失败的情况 返回统一的认证失败响应
	 * @return 认证入口点实例
	 */
	@Bean
	@ConditionalOnMissingBean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new CustomAuthenticationEntryPoint();
	}

	/**
	 * 访问拒绝处理器 用于处理权限不足的情况 返回统一的访问拒绝响应
	 * @return 访问拒绝处理器实例
	 */
	@Bean
	@ConditionalOnMissingBean
	public AccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}

	/**
	 * 远程Token服务提供者 优先使用token-info-uri校验token认证信息 适用于只需要验证token有效性的场景
	 * @return 远程Token服务提供者实例
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
	 * 用户信息Token服务提供者 当prefer-token-info设置为false或不配置token-info-uri时使用 适用于需要获取用户详细信息的场景
	 * @param restTemplateFactory REST模板工厂
	 * @param authoritiesExtractorProvider 权限提取器提供者
	 * @param principalExtractorProvider 主体提取器提供者
	 * @return 用户信息Token服务提供者实例
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

	/**
	 * 用户信息条件判断类 用于判断是否使用用户信息Token服务
	 */
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

	/**
	 * Token信息条件判断类 用于判断是否使用Token信息服务
	 */
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

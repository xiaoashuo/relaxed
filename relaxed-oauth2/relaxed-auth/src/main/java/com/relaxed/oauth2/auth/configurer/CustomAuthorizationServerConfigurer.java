package com.relaxed.oauth2.auth.configurer;

import com.relaxed.oauth2.auth.builder.TokenGrantBuilder;
import com.relaxed.oauth2.auth.builder.TokenServicesBuilder;
import com.relaxed.oauth2.auth.handler.AuthorizationInfoHandle;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义授权服务器配置器 继承自AuthorizationServerConfigurerAdapter，用于配置OAuth2授权服务器的各项功能： 1. 配置客户端详情服务
 * 2. 配置授权端点 3. 配置安全规则 支持自定义Token增强、异常处理、认证管理等扩展功能
 *
 * @author Yakir
 * @since 1.0
 */
@RequiredArgsConstructor
public class CustomAuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {

	/**
	 * OAuth2客户端配置器 用于配置客户端详情服务
	 */
	private final OAuth2ClientConfigurer clientConfigurer;

	/**
	 * 认证管理器 用于处理认证请求
	 */
	private final AuthenticationManager authenticationManager;

	/**
	 * 认证入口点 用于处理未认证的请求
	 */
	private final AuthenticationEntryPoint authenticationEntryPoint;

	/**
	 * 访问拒绝处理器 用于处理无权限访问的请求
	 */
	private final AccessDeniedHandler accessDeniedHandler;

	/**
	 * Token存储 用于存储和管理Token
	 */
	private final TokenStore tokenStore;

	/**
	 * Token授予构建器 用于构建和组合Token授予者
	 */
	private final TokenGrantBuilder tokenGrantBuilder;

	/**
	 * Token增强器列表 用于在Token中添加额外信息
	 */
	private final List<TokenEnhancer> tokenEnhancerList;

	/**
	 * 访问Token转换器 用于自定义Token的转换逻辑
	 */
	private final AccessTokenConverter accessTokenConverter;

	/**
	 * Web响应异常转换器 用于自定义异常处理
	 */
	private final WebResponseExceptionTranslator<OAuth2Exception> webResponseExceptionTranslator;

	/**
	 * 用户详情服务 用于加载用户信息
	 */
	private final UserDetailsService userDetailsService;

	/**
	 * Token服务构建器 用于构建Token服务
	 */
	private final TokenServicesBuilder tokenServicesBuilder;

	/**
	 * 配置授权服务器的安全规则 包括： 1. 配置Token访问权限 2. 配置认证入口点 3. 配置访问拒绝处理器 4. 允许客户端表单认证 5.
	 * 处理客户端认证过滤器的异常
	 * @param security 授权服务器安全配置器
	 * @throws Exception 配置过程中的异常
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		// @formatter:off
        security.tokenKeyAccess("permitAll()")  //开启 /oauth/token_key 的访问权限控制
                .checkTokenAccess("isAuthenticated()")
                .authenticationEntryPoint(authenticationEntryPoint)
				.accessDeniedHandler(accessDeniedHandler)
                //让/oauth/token支持client_id以及client_secret作登录认证，
                .allowFormAuthenticationForClients()

				// 处理使用 allowFormAuthenticationForClients 后，注册的过滤器异常处理不走自定义配置的问题
				.addObjectPostProcessor(new ObjectPostProcessor<Object>() {
					@Override
					public <O> O postProcess(O object) {
						if(object instanceof ClientCredentialsTokenEndpointFilter) {
							ClientCredentialsTokenEndpointFilter filter = (ClientCredentialsTokenEndpointFilter) object;
							filter.setAuthenticationEntryPoint(authenticationEntryPoint);

						}
						return object;
					}
				});
        ;

        // @formatter:on
	}

	/**
	 * 配置授权服务器的端点 包括： 1. 配置认证管理器 2. 配置用户详情服务 3. 配置Token存储策略 4. 配置Token授予者 5. 配置Token转换器 6.
	 * 配置Token增强器 7. 配置异常转换器 8. 配置Token服务
	 * @param endpoints 授权服务器端点配置器
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
		List<TokenEnhancer> delegates = new ArrayList<>();
		if (!CollectionUtils.isEmpty(tokenEnhancerList)) {
			delegates.addAll(tokenEnhancerList);
		}
		enhancerChain.setTokenEnhancers(delegates);
		endpoints.authenticationManager(authenticationManager).userDetailsService(userDetailsService)
				// 配置令牌存储策略
				.tokenStore(tokenStore)
				// 自定义tokenGranter
				.tokenGranter(tokenGrantBuilder.build(endpoints))

				// 使用自定义的 TokenConverter，方便在 checkToken 时，返回更多的信息
				.accessTokenConverter(accessTokenConverter)
				// TokenEnhancer作用是在OAuth2AccessToken里添加额外的信息。
				.tokenEnhancer(enhancerChain)
				// 自定义的认证时异常转换
				.exceptionTranslator(webResponseExceptionTranslator)
				.tokenServices(tokenServicesBuilder.build(endpoints));
	}

	/**
	 * 配置客户端详情服务 通过OAuth2ClientConfigurer配置客户端信息 客户端信息可以存储在代码、配置文件或数据库中
	 * @param clients 客户端详情服务配置器
	 * @throws Exception 配置过程中的异常
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clientConfigurer.configure(clients);
	}

}

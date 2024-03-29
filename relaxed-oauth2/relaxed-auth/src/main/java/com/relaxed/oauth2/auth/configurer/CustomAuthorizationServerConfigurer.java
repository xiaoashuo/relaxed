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
 * @author Yakir
 * @Topic CustomAuthorizationServerConfigurer
 * @Description 认证服务器配置
 * @date 2022/7/24 11:35
 * @Version 1.0
 */
@RequiredArgsConstructor
public class CustomAuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {

	private final OAuth2ClientConfigurer clientConfigurer;

	private final AuthenticationManager authenticationManager;

	private final AuthenticationEntryPoint authenticationEntryPoint;

	private final AccessDeniedHandler accessDeniedHandler;

	private final TokenStore tokenStore;

	private final TokenGrantBuilder tokenGrantBuilder;

	private final List<TokenEnhancer> tokenEnhancerList;

	private final AccessTokenConverter accessTokenConverter;

	private final WebResponseExceptionTranslator<OAuth2Exception> webResponseExceptionTranslator;

	private final UserDetailsService userDetailsService;

	private final TokenServicesBuilder tokenServicesBuilder;

	/**
	 * 定义资源权限控制的配置
	 * @param security AuthorizationServerSecurityConfigurer
	 * @throws Exception 异常
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
	 * 用来配置令牌的访问端点和令牌服务
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
	 * 用来配置客户端信息服务，客户端详情信息在这里初始化，可以写死在代码里，也可以放到配置文件或者数据库
	 * @param clients
	 * @throws Exception
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clientConfigurer.configure(clients);
	}

}

package com.relaxed.oauth2.auth.builder;

import com.relaxed.oauth2.auth.handler.AuthorizationInfoHandle;
import com.relaxed.oauth2.auth.extension.refresh.CustomPreAuthenticatedUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import java.util.Arrays;

/**
 * Token服务构建器 用于构建和配置OAuth2的Token服务 支持自定义Token存储、刷新Token和认证管理
 *
 * @author Yakir
 * @since 1.0
 */
@RequiredArgsConstructor
public class TokenServicesBuilder {

	/**
	 * 用户详情服务 用于加载用户信息
	 */
	private final UserDetailsService userDetailsService;

	/**
	 * 授权信息处理器 用于管理客户端和授权类型的映射
	 */
	private final AuthorizationInfoHandle authorizationInfoHandle;

	/**
	 * Token存储 用于存储和管理Token
	 */
	private final TokenStore tokenStore;

	/**
	 * 构建Token服务 配置Token服务的各项参数，包括： 1. Token存储 2. 刷新Token支持 3. 客户端详情服务 4. Token增强器 5.
	 * 认证管理器 6. 刷新Token重用策略
	 * @param endpoints 授权服务器端点配置器
	 * @return 配置完成的Token服务
	 */
	public DefaultTokenServices build(AuthorizationServerEndpointsConfigurer endpoints) {
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		// 设置Token存储
		tokenServices.setTokenStore(endpoints.getTokenStore());
		// 启用刷新Token支持
		tokenServices.setSupportRefreshToken(true);
		// 设置客户端详情服务
		tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
		// 设置Token增强器
		tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
		// 配置预认证提供者
		PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
		provider.setPreAuthenticatedUserDetailsService(new CustomPreAuthenticatedUserDetailsService<>(
				userDetailsService, authorizationInfoHandle, tokenStore));
		// 设置认证管理器
		tokenServices.setAuthenticationManager(new ProviderManager(Arrays.asList(provider)));
		// 配置刷新Token重用策略
		// refresh_token有两种使用方式：重复使用(true)、非重复使用(false)，默认为true
		// 1.重复使用：access_token过期刷新时， refresh token过期时间未改变，仍以初次生成的时间为准
		// 2.非重复使用：access_token过期刷新时，
		// refresh_token过期时间延续，在refresh_token有效期内刷新而无需失效再次登录
		tokenServices.setReuseRefreshToken(false);
		return tokenServices;
	}

}

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
 * @author Yakir
 * @Topic TokenServicesBuilder
 * @Description
 * @date 2022/7/28 21:04
 * @Version 1.0
 */
@RequiredArgsConstructor
public class TokenServicesBuilder {

	private final UserDetailsService userDetailsService;

	private final AuthorizationInfoHandle authorizationInfoHandle;

	private final TokenStore tokenStore;

	public DefaultTokenServices build(AuthorizationServerEndpointsConfigurer endpoints) {
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setTokenStore(endpoints.getTokenStore());
		tokenServices.setSupportRefreshToken(true);
		tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
		tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
		PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
		provider.setPreAuthenticatedUserDetailsService(new CustomPreAuthenticatedUserDetailsService<>(
				userDetailsService, authorizationInfoHandle, tokenStore));
		tokenServices.setAuthenticationManager(new ProviderManager(Arrays.asList(provider)));
		// refresh_token有两种使用方式：重复使用(true)、非重复使用(false)，默认为true
		// 1.重复使用：access_token过期刷新时， refresh token过期时间未改变，仍以初次生成的时间为准
		// 2.非重复使用：access_token过期刷新时，
		// refresh_token过期时间延续，在refresh_token有效期内刷新而无需失效再次登录
		tokenServices.setReuseRefreshToken(false);
		return tokenServices;
	}

}

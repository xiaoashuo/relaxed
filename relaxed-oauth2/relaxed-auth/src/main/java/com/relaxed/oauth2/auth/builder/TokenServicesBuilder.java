package com.relaxed.oauth2.auth.builder;

import com.relaxed.oauth2.auth.extension.handler.AuthorizationInfoHandle;
import com.relaxed.oauth2.auth.extension.refresh.CustomPreAuthenticatedUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

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

	public DefaultTokenServices build(AuthorizationServerEndpointsConfigurer endpoints) {
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setTokenStore(endpoints.getTokenStore());
		tokenServices.setSupportRefreshToken(true);
		tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
		tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
		tokenServices.setReuseRefreshToken(false);
		PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
		provider.setPreAuthenticatedUserDetailsService(
				new CustomPreAuthenticatedUserDetailsService<>(userDetailsService, authorizationInfoHandle));
		tokenServices.setAuthenticationManager(new ProviderManager(Arrays.asList(provider)));
		return tokenServices;
	}

}

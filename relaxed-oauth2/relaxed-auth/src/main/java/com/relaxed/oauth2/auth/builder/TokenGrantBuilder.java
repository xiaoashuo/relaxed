package com.relaxed.oauth2.auth.builder;

import com.relaxed.oauth2.auth.extension.mobile.SmsCodeTokenGranter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Yakir
 * @Topic TokenGrantBuilder
 * @Description
 * @date 2022/7/22 18:07
 * @Version 1.0
 */
public class TokenGrantBuilder {

	private final AuthenticationManager authenticationManager;

	private final List<TokenGranterProvider> tokenGranterProviders;

	public TokenGrantBuilder(AuthenticationManager authenticationManager) {
		this(authenticationManager, Collections.EMPTY_LIST);
	}

	public TokenGrantBuilder(AuthenticationManager authenticationManager,
			List<TokenGranterProvider> tokenGranterProviders) {
		this.authenticationManager = authenticationManager;
		this.tokenGranterProviders = tokenGranterProviders;
	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public TokenGranter build(final AuthorizationServerEndpointsConfigurer endpoints) {
		List<TokenGranter> tokenGranters = defaultTokenGranters(endpoints);
		for (TokenGranterProvider tokenGranterProvider : tokenGranterProviders) {
			tokenGranters.add(tokenGranterProvider.provide(endpoints));
		}
		return new CompositeTokenGranter(tokenGranters);
	}

	/**
	 * OAuth2 规范的 5 种授权类型
	 * @param endpoints AuthorizationServerEndpointsConfigurer
	 * @return List<TokenGranter>
	 */
	protected List<TokenGranter> defaultTokenGranters(AuthorizationServerEndpointsConfigurer endpoints) {
		// 获取原有默认授权模式(授权码模式、密码模式、客户端模式、简化模式)的授权者
		List<TokenGranter> granterList = new ArrayList<>(Arrays.asList(endpoints.getTokenGranter()));

		return granterList;
	}

}

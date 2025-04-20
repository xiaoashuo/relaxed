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
 * Token授予构建器 用于构建和组合OAuth2的Token授予者 支持默认授权类型和自定义授权类型的组合
 *
 * @author Yakir
 * @since 1.0
 */
public class TokenGrantBuilder {

	/**
	 * 认证管理器 用于处理认证请求
	 */
	private final AuthenticationManager authenticationManager;

	/**
	 * Token授予者提供者列表 用于提供自定义的Token授予者
	 */
	private final List<TokenGranterProvider> tokenGranterProviders;

	/**
	 * 构造函数 使用默认的空Token授予者提供者列表
	 * @param authenticationManager 认证管理器
	 */
	public TokenGrantBuilder(AuthenticationManager authenticationManager) {
		this(authenticationManager, Collections.EMPTY_LIST);
	}

	/**
	 * 构造函数 使用指定的Token授予者提供者列表
	 * @param authenticationManager 认证管理器
	 * @param tokenGranterProviders Token授予者提供者列表
	 */
	public TokenGrantBuilder(AuthenticationManager authenticationManager,
			List<TokenGranterProvider> tokenGranterProviders) {
		this.authenticationManager = authenticationManager;
		this.tokenGranterProviders = tokenGranterProviders;
	}

	/**
	 * 获取认证管理器
	 * @return 认证管理器
	 */
	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	/**
	 * 构建Token授予者 组合默认授权类型和自定义授权类型的Token授予者
	 * @param endpoints 授权服务器端点配置器
	 * @return 组合后的Token授予者
	 */
	public TokenGranter build(final AuthorizationServerEndpointsConfigurer endpoints) {
		List<TokenGranter> tokenGranters = defaultTokenGranters(endpoints);
		for (TokenGranterProvider tokenGranterProvider : tokenGranterProviders) {
			tokenGranters.add(tokenGranterProvider.provide(endpoints));
		}
		return new CompositeTokenGranter(tokenGranters);
	}

	/**
	 * 获取默认Token授予者列表 包含OAuth2规范的5种授权类型： 1. 授权码模式 2. 密码模式 3. 客户端模式 4. 简化模式 5. 刷新Token模式
	 * @param endpoints 授权服务器端点配置器
	 * @return 默认Token授予者列表
	 */
	protected List<TokenGranter> defaultTokenGranters(AuthorizationServerEndpointsConfigurer endpoints) {
		// 获取原有默认授权模式(授权码模式、密码模式、客户端模式、简化模式)的授权者
		List<TokenGranter> granterList = new ArrayList<>(Arrays.asList(endpoints.getTokenGranter()));

		return granterList;
	}

}

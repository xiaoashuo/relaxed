package com.relaxed.oauth2.auth.extension.refresh;

import com.relaxed.oauth2.auth.functions.RetriveUserFunction;
import com.relaxed.oauth2.auth.handler.AuthorizationInfoHandle;
import com.relaxed.oauth2.auth.util.RequestUtil;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.Assert;

/**
 * 刷新Token认证用户详情服务 用于处理OAuth2刷新令牌时的用户认证 支持根据不同的授权类型选择不同的用户信息获取方式
 *
 * @author <a href="mailto:xianrui0365@163.com">haoxr</a>
 * @since 1.0
 */
@NoArgsConstructor
public class CustomPreAuthenticatedUserDetailsService<T extends Authentication>
		implements AuthenticationUserDetailsService<T>, InitializingBean {

	/**
	 * 用户详情服务 用于加载用户基本信息
	 */
	private UserDetailsService userDetailsService;

	/**
	 * 授权信息处理器 用于处理不同类型的授权方式
	 */
	private AuthorizationInfoHandle authorizationInfoHandle;

	/**
	 * Token存储服务 用于读取和验证刷新令牌
	 */
	private TokenStore tokenStore;

	/**
	 * 构造函数
	 * @param userDetailsService 用户详情服务
	 * @param authorizationInfoHandle 授权信息处理器
	 * @param tokenStore Token存储服务
	 */
	public CustomPreAuthenticatedUserDetailsService(UserDetailsService userDetailsService,
			AuthorizationInfoHandle authorizationInfoHandle, TokenStore tokenStore) {
		Assert.notNull(userDetailsService, "UserDetailsService cannot be null.");
		this.userDetailsService = userDetailsService;
		this.authorizationInfoHandle = authorizationInfoHandle;
		this.tokenStore = tokenStore;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.userDetailsService, "UserDetailsService must be set");
	}

	/**
	 * 加载用户详情信息 根据刷新令牌和认证信息获取用户详情 支持根据不同的授权类型选择不同的用户信息获取方式
	 * @param authentication 认证信息
	 * @return 用户详情信息
	 * @throws UsernameNotFoundException 当用户不存在时抛出
	 */
	@Override
	public UserDetails loadUserDetails(T authentication) throws UsernameNotFoundException {
		String refreshToken = RequestUtil.getRequestRefreshToken();
		OAuth2Authentication oAuth2Authentication = tokenStore
				.readAuthenticationForRefreshToken(tokenStore.readRefreshToken(refreshToken));
		OAuth2Request oAuth2Request = oAuth2Authentication.getOAuth2Request();
		String grantType = oAuth2Request.getGrantType();
		RetriveUserFunction retriveUserFunction = authorizationInfoHandle.obtainFunction(grantType);
		if (retriveUserFunction == null) {
			return userDetailsService.loadUserByUsername(authentication.getName());
		}
		return retriveUserFunction.retrive(authentication, userDetailsService);
	}

}

package com.relaxed.oauth2.auth.extension.refresh;

import com.relaxed.oauth2.auth.extension.functions.RetriveUserFunction;
import com.relaxed.oauth2.auth.extension.handler.AuthorizationInfoHandle;
import com.relaxed.oauth2.auth.util.RequestUtil;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

/**
 * 刷新token再次认证 UserDetailsService
 *
 * @author <a href="mailto:xianrui0365@163.com">haoxr</a>
 * @date 2021/10/2
 */
@NoArgsConstructor
public class CustomPreAuthenticatedUserDetailsService<T extends Authentication>
		implements AuthenticationUserDetailsService<T>, InitializingBean {

	/**
	 * UserDetailService 的映射
	 *
	 * @see
	 */
	private UserDetailsService userDetailsService;

	private AuthorizationInfoHandle authorizationInfoHandle;

	public CustomPreAuthenticatedUserDetailsService(UserDetailsService userDetailsService,
			AuthorizationInfoHandle authorizationInfoHandle) {
		Assert.notNull(userDetailsService, "UserDetailsService cannot be null.");
		this.userDetailsService = userDetailsService;
		this.authorizationInfoHandle = authorizationInfoHandle;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.userDetailsService, "UserDetailsService must be set");
	}

	/**
	 * 重写PreAuthenticatedAuthenticationProvider 的 preAuthenticatedUserDetailsService
	 * 属性，可根据客户端和认证方式选择用户服务 UserDetailService 获取用户信息 UserDetail
	 * @param authentication
	 * @return
	 * @throws UsernameNotFoundException
	 */
	@Override
	public UserDetails loadUserDetails(T authentication) throws UsernameNotFoundException {
		String grantType = RequestUtil.getGrantType();
		RetriveUserFunction retriveUserFunction = authorizationInfoHandle.obtainFunction(grantType);
		if (retriveUserFunction == null) {
			return userDetailsService.loadUserByUsername(authentication.getName());
		}
		return retriveUserFunction.retrive(authentication, userDetailsService);
	}

}

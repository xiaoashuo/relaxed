package com.relaxed.oauth2.auth.extension.mobile;

import com.relaxed.oauth2.auth.extension.ExtendUserDetailsService;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.Assert;

import java.util.HashSet;

/**
 * 短信验证码认证提供者 实现基于手机验证码的用户认证逻辑 通过手机号获取用户信息并完成认证
 *
 * @author <a href="mailto:xianrui0365@163.com">haoxr</a>
 * @since 1.0
 */
@Data
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

	/**
	 * 用户详情服务 用于根据手机号获取用户信息
	 */
	private final UserDetailsService userDetailsService;

	/**
	 * 执行认证 根据手机号获取用户信息并创建认证令牌
	 * @param authentication 认证请求
	 * @return 认证结果
	 * @throws AuthenticationException 当认证失败时抛出
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
		String mobile = (String) authenticationToken.getPrincipal();
		UserDetails userDetails = ((ExtendUserDetailsService) userDetailsService).loginByMobile(mobile);
		SmsCodeAuthenticationToken result = new SmsCodeAuthenticationToken(userDetails, authentication.getCredentials(),
				new HashSet<>());
		result.setDetails(authentication.getDetails());
		return result;
	}

	/**
	 * 判断是否支持指定的认证类型
	 * @param authentication 认证类型
	 * @return 如果支持该认证类型则返回true
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
	}

}

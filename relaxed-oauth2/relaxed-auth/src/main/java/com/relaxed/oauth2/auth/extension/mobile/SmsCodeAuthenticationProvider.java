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
 * 短信验证码认证授权提供者
 *
 * @author <a href="mailto:xianrui0365@163.com">haoxr</a>
 * @date 2021/9/25
 */
@Data
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

	private final SmsCodeValidator smsCodeValidator;

	private final UserDetailsService userDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
		String mobile = (String) authenticationToken.getPrincipal();
		String code = (String) authenticationToken.getCredentials();
		Assert.notNull(smsCodeValidator, "短信验证器不能为空");
		// 认证手机号验证码
		smsCodeValidator.authenticate(mobile, code);
		UserDetails userDetails = ((ExtendUserDetailsService) userDetailsService).loginByMobile(mobile);
		SmsCodeAuthenticationToken result = new SmsCodeAuthenticationToken(userDetails, authentication.getCredentials(),
				new HashSet<>());
		result.setDetails(authentication.getDetails());
		return result;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
	}

}

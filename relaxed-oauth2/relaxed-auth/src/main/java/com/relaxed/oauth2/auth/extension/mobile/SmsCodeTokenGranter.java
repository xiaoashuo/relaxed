package com.relaxed.oauth2.auth.extension.mobile;

import com.relaxed.oauth2.auth.extension.PreValidator;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.util.Assert;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 手机验证码授权者
 *
 * @author <a href="mailto:xianrui0365@163.com">haoxr</a>
 * @date 2021/9/25
 */
public class SmsCodeTokenGranter extends AbstractTokenGranter {

	/**
	 * 声明授权者 SmsCodeTokenGranter 支持授权模式 sms_code 根据接口传值 grant_type = sms_code 的值匹配到此授权者
	 * 匹配逻辑详见下面的两个方法
	 *
	 * @see CompositeTokenGranter#grant(String, TokenRequest)
	 * @see AbstractTokenGranter#grant(String, TokenRequest)
	 */
	public static final String GRANT_TYPE = "sms_code";

	private final AuthenticationManager authenticationManager;

	private final PreValidator preValidator;

	public SmsCodeTokenGranter(AuthorizationServerTokenServices tokenServices,
			ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory,
			AuthenticationManager authenticationManager, PreValidator preValidator) {
		super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
		Assert.notNull(preValidator, "前置验证器不能为空-[sms_code]");
		this.authenticationManager = authenticationManager;
		this.preValidator = preValidator;
	}

	@Override
	protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {

		Map<String, String> parameters = new LinkedHashMap(tokenRequest.getRequestParameters());
		preValidator.validate(parameters);
		String mobile = parameters.get("mobile"); // 手机号
		String code = parameters.get("code"); // 短信验证码
		parameters.remove("code");

		Authentication userAuth = new SmsCodeAuthenticationToken(mobile, code);
		((AbstractAuthenticationToken) userAuth).setDetails(parameters);

		try {
			userAuth = this.authenticationManager.authenticate(userAuth);
		}
		catch (AccountStatusException exception) {
			throw new InvalidGrantException(exception.getMessage());
		}
		catch (BadCredentialsException exception) {
			throw new InvalidGrantException(exception.getMessage());
		}

		if (userAuth != null && userAuth.isAuthenticated()) {
			OAuth2Request storedOAuth2Request = this.getRequestFactory().createOAuth2Request(client, tokenRequest);
			return new OAuth2Authentication(storedOAuth2Request, userAuth);
		}
		else {
			throw new InvalidGrantException("Could not authenticate user: " + mobile);
		}
	}

}

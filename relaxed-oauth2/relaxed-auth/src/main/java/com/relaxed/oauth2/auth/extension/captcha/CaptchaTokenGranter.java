package com.relaxed.oauth2.auth.extension.captcha;

import com.relaxed.oauth2.auth.extension.PreValidator;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.util.Assert;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 验证码授权模式授权者
 *
 * @author <a href="mailto:xianrui0365@163.com">haoxr</a>
 * @date 2021/9/25
 */
public class CaptchaTokenGranter extends AbstractTokenGranter {

	/**
	 * 声明授权者 CaptchaTokenGranter 支持授权模式 captcha 根据接口传值 grant_type = captcha 的值匹配到此授权者
	 * 匹配逻辑详见下面的两个方法
	 *
	 * @see CompositeTokenGranter#grant(String, TokenRequest)
	 * @see AbstractTokenGranter#grant(String, TokenRequest)
	 */
	public static final String GRANT_TYPE = "captcha";

	private final AuthenticationManager authenticationManager;

	private final PreValidator preValidator;

	public CaptchaTokenGranter(AuthorizationServerTokenServices tokenServices,
			ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory,
			AuthenticationManager authenticationManager, PreValidator preValidator) {
		super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
		Assert.notNull(preValidator, "前置验证器不能为空-[captcha]");
		this.authenticationManager = authenticationManager;
		this.preValidator = preValidator;
	}

	@Override
	protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {

		Map<String, String> parameters = new LinkedHashMap(tokenRequest.getRequestParameters());
		// 验证验证码正确性
		preValidator.validate(parameters);
		String username = parameters.get("username");
		String password = parameters.get("password");
		// 移除后续无用参数
		parameters.remove("password");
		// 和密码模式一样的逻辑
		Authentication userAuth = new UsernamePasswordAuthenticationToken(username, password);
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
			throw new InvalidGrantException("Could not authenticate user: " + username);
		}
	}

}

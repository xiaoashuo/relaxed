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
 * 手机验证码授权者 实现基于手机验证码的OAuth2授权流程 支持通过手机号和验证码进行用户认证并获取访问令牌
 *
 * @author <a href="mailto:xianrui0365@163.com">haoxr</a>
 * @since 1.0
 */
public class SmsCodeTokenGranter extends AbstractTokenGranter {

	/**
	 * 授权类型标识 用于标识手机验证码授权方式 当请求中的grant_type参数值为sms_code时，将使用此授权者处理请求
	 */
	public static final String GRANT_TYPE = "sms_code";

	/**
	 * 认证管理器 用于处理用户认证逻辑
	 */
	private final AuthenticationManager authenticationManager;

	/**
	 * 预验证器 用于在认证前进行额外的验证操作
	 */
	private final PreValidator preValidator;

	/**
	 * 构造函数
	 * @param tokenServices Token服务
	 * @param clientDetailsService 客户端详情服务
	 * @param requestFactory 请求工厂
	 * @param authenticationManager 认证管理器
	 * @param preValidator 预验证器
	 */
	public SmsCodeTokenGranter(AuthorizationServerTokenServices tokenServices,
			ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory,
			AuthenticationManager authenticationManager, PreValidator preValidator) {
		super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
		Assert.notNull(preValidator, "前置验证器不能为空-[sms_code]");
		this.authenticationManager = authenticationManager;
		this.preValidator = preValidator;
	}

	/**
	 * 获取OAuth2认证信息 处理手机验证码认证流程，包括： 1. 验证请求参数 2. 执行预验证 3. 创建并验证认证令牌 4. 返回OAuth2认证信息
	 * @param client 客户端详情
	 * @param tokenRequest Token请求
	 * @return OAuth2认证信息
	 * @throws InvalidGrantException 当认证失败时抛出
	 */
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

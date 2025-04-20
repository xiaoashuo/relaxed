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
 * 验证码授权者 实现基于验证码的OAuth2授权流程 支持用户名密码登录，并在认证前进行验证码校验
 *
 * @author <a href="mailto:xianrui0365@163.com">haoxr</a>
 * @since 1.0
 */
public class CaptchaTokenGranter extends AbstractTokenGranter {

	/**
	 * 授权类型标识 用于标识验证码授权方式 当请求中的grant_type参数值为captcha时，将使用此授权者处理请求
	 */
	public static final String GRANT_TYPE = "captcha";

	/**
	 * 认证管理器 用于处理用户认证逻辑
	 */
	private final AuthenticationManager authenticationManager;

	/**
	 * 预验证器 用于在认证前进行验证码校验
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
	public CaptchaTokenGranter(AuthorizationServerTokenServices tokenServices,
			ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory,
			AuthenticationManager authenticationManager, PreValidator preValidator) {
		super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
		Assert.notNull(preValidator, "前置验证器不能为空-[captcha]");
		this.authenticationManager = authenticationManager;
		this.preValidator = preValidator;
	}

	/**
	 * 获取OAuth2认证信息 处理验证码认证流程，包括： 1. 验证验证码 2. 提取用户名和密码 3. 创建并验证认证令牌 4. 返回OAuth2认证信息
	 * @param client 客户端详情
	 * @param tokenRequest Token请求
	 * @return OAuth2认证信息
	 * @throws InvalidGrantException 当认证失败时抛出
	 */
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

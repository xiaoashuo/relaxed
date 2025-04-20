package com.relaxed.oauth2.resource.services;

import com.relaxed.common.model.result.SysResultCode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.util.Assert;

import java.util.*;

/**
 * 自定义用户信息Token服务 用于从用户信息端点获取用户信息并构建认证信息 支持自定义权限提取器和主体提取器
 *
 * @author Yakir
 * @since 1.0
 */
public class CustomUserInfoTokenServices implements ResourceServerTokenServices {

	/**
	 * 日志记录器
	 */
	protected final Log logger = LogFactory.getLog(this.getClass());

	/**
	 * 用户信息端点URL
	 */
	private final String userInfoEndpointUrl;

	/**
	 * 客户端ID
	 */
	private final String clientId;

	/**
	 * OAuth2 REST操作模板
	 */
	private OAuth2RestOperations restTemplate;

	/**
	 * Token类型，默认为Bearer
	 */
	private String tokenType = "Bearer";

	/**
	 * 权限提取器，用于从用户信息中提取权限
	 */
	private AuthoritiesExtractor authoritiesExtractor = new FixedAuthoritiesExtractor();

	/**
	 * 主体提取器，用于从用户信息中提取主体信息
	 */
	private PrincipalExtractor principalExtractor = new FixedPrincipalExtractor();

	/**
	 * 构造函数
	 * @param userInfoEndpointUrl 用户信息端点URL
	 * @param clientId 客户端ID
	 */
	public CustomUserInfoTokenServices(String userInfoEndpointUrl, String clientId) {
		this.userInfoEndpointUrl = userInfoEndpointUrl;
		this.clientId = clientId;
	}

	/**
	 * 设置Token类型
	 * @param tokenType Token类型
	 */
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	/**
	 * 设置REST操作模板
	 * @param restTemplate OAuth2 REST操作模板
	 */
	public void setRestTemplate(OAuth2RestOperations restTemplate) {
		this.restTemplate = restTemplate;
	}

	/**
	 * 设置权限提取器
	 * @param authoritiesExtractor 权限提取器
	 */
	public void setAuthoritiesExtractor(AuthoritiesExtractor authoritiesExtractor) {
		Assert.notNull(authoritiesExtractor, "AuthoritiesExtractor must not be null");
		this.authoritiesExtractor = authoritiesExtractor;
	}

	/**
	 * 设置主体提取器
	 * @param principalExtractor 主体提取器
	 */
	public void setPrincipalExtractor(PrincipalExtractor principalExtractor) {
		Assert.notNull(principalExtractor, "PrincipalExtractor must not be null");
		this.principalExtractor = principalExtractor;
	}

	/**
	 * 加载认证信息 通过用户信息端点获取用户信息并构建认证信息
	 * @param accessToken 访问令牌
	 * @return OAuth2认证信息
	 * @throws AuthenticationException 认证异常
	 * @throws InvalidTokenException 无效令牌异常
	 */
	@Override
	public OAuth2Authentication loadAuthentication(String accessToken)
			throws AuthenticationException, InvalidTokenException {
		// 获取用户信息
		Map<String, Object> map = this.getMap(this.userInfoEndpointUrl, accessToken);

		// 检查响应结果
		if (map.containsKey("code") && !SysResultCode.SUCCESS.getCode().equals(map.get("code"))) {
			if (this.logger.isDebugEnabled()) {
				this.logger.debug("userinfo returned error: " + map.get("error"));
			}

			throw new InvalidTokenException(accessToken);
		}
		else {
			// 提取认证信息
			return this.extractAuthentication(map);
		}
	}

	/**
	 * 从用户信息中提取认证信息
	 * @param map 用户信息Map
	 * @return OAuth2认证信息
	 */
	private OAuth2Authentication extractAuthentication(Map<String, Object> map) {
		// 获取主体信息
		Object principal = this.getPrincipal(map);
		// 提取权限信息
		List<GrantedAuthority> authorities = this.authoritiesExtractor.extractAuthorities(map);
		// 构建OAuth2请求
		OAuth2Request request = new OAuth2Request((Map) null, this.clientId, (Collection) null, true, (Set) null,
				(Set) null, (String) null, (Set) null, (Map) null);
		// 构建认证令牌
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, "N/A",
				authorities);
		token.setDetails(map);
		return new OAuth2Authentication(request, token);
	}

	/**
	 * 获取主体信息
	 * @param map 用户信息Map
	 * @return 主体信息，如果为空则返回"unknown"
	 */
	protected Object getPrincipal(Map<String, Object> map) {
		Object principal = this.principalExtractor.extractPrincipal(map);
		return principal == null ? "unknown" : principal;
	}

	/**
	 * 读取访问令牌 不支持此操作
	 * @param accessToken 访问令牌
	 * @return 不支持此操作
	 */
	@Override
	public OAuth2AccessToken readAccessToken(String accessToken) {
		throw new UnsupportedOperationException("Not supported: read access token");
	}

	/**
	 * 从用户信息端点获取用户信息
	 * @param path 用户信息端点URL
	 * @param accessToken 访问令牌
	 * @return 用户信息Map
	 */
	private Map<String, Object> getMap(String path, String accessToken) {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Getting user info from: " + path);
		}

		try {
			// 获取或创建REST模板
			OAuth2RestOperations restTemplate = this.restTemplate;
			if (restTemplate == null) {
				BaseOAuth2ProtectedResourceDetails resource = new BaseOAuth2ProtectedResourceDetails();
				resource.setClientId(this.clientId);
				restTemplate = new OAuth2RestTemplate(resource);
			}

			// 设置访问令牌
			OAuth2AccessToken existingToken = ((OAuth2RestOperations) restTemplate).getOAuth2ClientContext()
					.getAccessToken();
			if (existingToken == null || !accessToken.equals(existingToken.getValue())) {
				DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(accessToken);
				token.setTokenType(this.tokenType);
				((OAuth2RestOperations) restTemplate).getOAuth2ClientContext().setAccessToken(token);
			}

			// 发送请求获取用户信息
			return (Map) ((OAuth2RestOperations) restTemplate).getForEntity(path, Map.class, new Object[0]).getBody();
		}
		catch (Exception var6) {
			this.logger.warn("Could not fetch user details: " + var6.getClass() + ", " + var6.getMessage());
			return Collections.singletonMap("error", "Could not fetch user details");
		}
	}

}

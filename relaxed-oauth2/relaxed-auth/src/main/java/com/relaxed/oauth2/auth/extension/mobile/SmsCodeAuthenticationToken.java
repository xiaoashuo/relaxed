package com.relaxed.oauth2.auth.extension.mobile;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * 手机验证码认证令牌 用于封装手机验证码认证过程中的认证信息 包含手机号（principal）和验证码（credentials）等信息
 *
 * @author <a href="mailto:xianrui0365@163.com">haoxr</a>
 * @since 1.0
 */
public class SmsCodeAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 550L;

	/**
	 * 认证主体，通常为手机号
	 */
	private Object principal;

	/**
	 * 认证凭证，通常为验证码
	 */
	private Object credentials;

	/**
	 * 创建未认证的令牌
	 * @param principal 认证主体（手机号）
	 * @param credentials 认证凭证（验证码）
	 */
	public SmsCodeAuthenticationToken(Object principal, Object credentials) {
		super((Collection) null);
		this.principal = principal;
		this.credentials = credentials;
		this.setAuthenticated(false);
	}

	/**
	 * 创建已认证的令牌
	 * @param principal 认证主体（用户信息）
	 * @param credentials 认证凭证（验证码）
	 * @param authorities 用户权限列表
	 */
	public SmsCodeAuthenticationToken(Object principal, Object credentials,
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.credentials = credentials;
		super.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return this.credentials;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

	/**
	 * 设置认证状态 只能将令牌设置为未认证状态
	 * @param isAuthenticated 认证状态
	 * @throws IllegalArgumentException 当尝试将令牌设置为已认证状态时抛出
	 */
	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		Assert.isTrue(!isAuthenticated,
				"Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
		super.setAuthenticated(false);
	}

	/**
	 * 清除敏感信息 清除认证凭证，防止敏感信息泄露
	 */
	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
		this.credentials = null;
	}

}

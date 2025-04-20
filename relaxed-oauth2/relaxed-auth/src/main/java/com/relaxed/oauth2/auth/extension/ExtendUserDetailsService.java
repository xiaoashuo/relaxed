package com.relaxed.oauth2.auth.extension;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 扩展的用户详情服务接口 继承自Spring Security的UserDetailsService，提供额外的用户认证方式 主要用于支持手机号登录等扩展的认证方式
 *
 * @author Yakir
 * @since 1.0.0
 */
public interface ExtendUserDetailsService extends UserDetailsService {

	/**
	 * 通过手机号加载用户信息 用于支持手机号登录的认证方式
	 * @param mobile 用户手机号
	 * @return UserDetails 用户详情信息
	 * @author Yakir
	 * @since 1.0.0
	 */
	UserDetails loginByMobile(String mobile);

}

package com.relaxed.oauth2.auth.functions;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 用户信息检索函数接口 用于自定义从认证信息中获取用户详情的逻辑 支持不同类型的认证方式，如用户名密码、短信验证码等
 *
 * @author Yakir
 * @since 1.0
 */
@FunctionalInterface
public interface RetriveUserFunction {

	/**
	 * 从认证信息中检索用户详情 根据认证类型和用户详情服务获取完整的用户信息
	 * @param authentication 认证信息，包含用户身份验证的详细信息
	 * @param userDetailsService 用户详情服务，用于加载用户信息
	 * @param <T> 认证类型，必须是Authentication的子类
	 * @return 用户详情对象，包含用户的完整信息
	 */
	<T extends Authentication> UserDetails retrive(T authentication, UserDetailsService userDetailsService);

}

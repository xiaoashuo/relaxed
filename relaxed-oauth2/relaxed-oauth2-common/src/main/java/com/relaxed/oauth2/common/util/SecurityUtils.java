package com.relaxed.oauth2.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 安全工具类 提供获取Spring Security上下文和认证信息的方法 用于在应用中方便地获取当前用户的安全信息
 *
 * @author Yakir
 * @since 1.0
 */
public class SecurityUtils {

	/**
	 * 获取当前认证信息 包含用户身份、权限等信息
	 * @return 当前认证信息，如果未认证则返回null
	 */
	public static Authentication getAuthentication() {
		return getSecurityContext().getAuthentication();
	}

	/**
	 * 获取安全上下文 包含当前线程的安全相关信息
	 * @return 当前安全上下文
	 */
	public static SecurityContext getSecurityContext() {
		return SecurityContextHolder.getContext();
	}

}

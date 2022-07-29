package com.relaxed.oauth2.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Yakir
 * @Topic SecurityUtils
 * @Description
 * @date 2022/7/29 11:00
 * @Version 1.0
 */
public class SecurityUtils {

	/**
	 * 获取Authentication
	 */
	public static Authentication getAuthentication() {
		return getSecurityContext().getAuthentication();
	}

	/**
	 * 获取SecurityContext
	 */
	public static SecurityContext getSecurityContext() {
		return SecurityContextHolder.getContext();
	}

}

package com.relaxed.common.security.jwt.tookit;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Yakir
 * @Topic SecurityUtils
 * @Description
 * @date 2021/8/18 13:40
 * @Version 1.0
 */
public class SecurityUtils {

	/**
	 * 获取用户信息
	 * @author yakir
	 * @date 2021/8/18 15:01
	 * @return T
	 */
	public static <T extends UserDetails> T getUser() {
		return getUser(getAuthentication());
	}

	private static <T extends UserDetails> T getUser(Authentication authentication) {
		return (T) Optional.ofNullable(authentication).map(Authentication::getPrincipal).orElseGet(() -> null);
	}

	private static SecurityContext getSecurityContext() {
		return SecurityContextHolder.getContext();
	}

	/**
	 * 获取Authentication
	 * @author yakir
	 * @date 2021/8/18 15:01
	 * @return org.springframework.security.core.Authentication
	 */
	public static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

}

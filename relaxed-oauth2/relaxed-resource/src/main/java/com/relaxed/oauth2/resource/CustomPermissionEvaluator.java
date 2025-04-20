package com.relaxed.oauth2.resource;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * 自定义权限评估器 用于评估用户是否具有特定权限 支持通配符匹配权限字符串
 *
 * @author Yakir
 * @since 1.0
 */
@Slf4j
public class CustomPermissionEvaluator {

	/**
	 * 判断当前用户是否具有指定权限 支持通配符匹配，如：user:read、user:write、user:*等
	 * @param permission 权限字符串，格式为xxx:xxx
	 * @return 具有权限返回true，否则返回false
	 */
	public boolean hasPermission(String permission) {
		// 权限为空直接返回false
		if (StrUtil.isBlank(permission)) {
			return false;
		}

		// 获取当前认证信息
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return false;
		}

		// 获取用户权限列表
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		// 检查用户权限列表中是否包含指定权限
		// 支持通配符匹配，如：user:read、user:write、user:*等
		return authorities.stream().map(GrantedAuthority::getAuthority).filter(StringUtils::hasText)
				.anyMatch(x -> PatternMatchUtils.simpleMatch(permission, x));
	}

}

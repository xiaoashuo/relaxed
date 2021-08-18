package com.relaxed.common.security.jwt.tookit;

import cn.hutool.core.util.StrUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * @author Yakir
 * @Topic CustomPermissionEvaluator
 * @Description 参考 ExpressionUrlAuthorizationConfigurer
 * @date 2021/8/18 11:39
 * @Version 1.0
 */
public class CustomPermissionEvaluator implements IPermissionEvaluator {

	/**
	 * 判断是否有权限
	 * @author yakir
	 * @date 2021/8/18 15:03
	 * @param permission
	 * @return boolean
	 */
	@Override
	public boolean hasPermission(String permission) {
		if (StrUtil.isBlank(permission)) {
			return false;
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return false;
		}
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		return authorities.stream().map(GrantedAuthority::getAuthority).filter(StringUtils::hasText)
				.anyMatch(x -> PatternMatchUtils.simpleMatch(permission, x));
	}

}

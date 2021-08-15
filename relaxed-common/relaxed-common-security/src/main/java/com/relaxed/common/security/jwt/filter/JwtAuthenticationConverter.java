package com.relaxed.common.security.jwt.filter;

import com.relaxed.common.security.jwt.core.Constants;
import com.relaxed.common.security.jwt.filter.JwtAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Yakir
 * @Topic JwtAuthenticationConverter
 * @Description
 * @date 2021/8/15 10:53
 * @Version 1.0
 */
public class JwtAuthenticationConverter implements AuthenticationConverter {

	protected AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

	/**
	 * 从请求头获取token
	 * @author yakir
	 * @date 2021/8/15 10:07
	 * @param request
	 * @return java.lang.String
	 */
	private String getToken(HttpServletRequest request) {
		return request.getHeader(Constants.AUTHORIZATION);
	}

	@Override
	public Authentication convert(HttpServletRequest request) {
		String token = getToken(request);
		if (!StringUtils.hasText(token)) {
			return null;
		}
		JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(token);
		this.setDetails(request, jwtAuthenticationToken);
		return jwtAuthenticationToken;
	}

	private void setDetails(HttpServletRequest request, JwtAuthenticationToken jwtAuthenticationToken) {
		jwtAuthenticationToken.setDetails(authenticationDetailsSource.buildDetails(request));
	}

}

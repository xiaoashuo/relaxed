package com.relaxed.common.security.jwt.handler;

import com.relaxed.common.security.jwt.core.Constants;
import com.relaxed.common.security.jwt.filter.JwtAuthenticationToken;
import com.relaxed.common.security.jwt.core.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * @author Yakir
 * @Topic JwtAuthenticationSuccessHandler
 * @Description
 * @date 2021/8/15 10:27
 * @Version 1.0
 */
@RequiredArgsConstructor
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final JwtTokenService jwtTokenService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Authentication authentication) {
		JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
		boolean shouldRefresh = jwtTokenService.shouldTokenRefresh(jwtAuthenticationToken.getToken());
		// 若应该刷新token 则执行刷新处理
		if (shouldRefresh) {
			String token = jwtTokenService.generateToken((UserDetails) authentication.getPrincipal());
			httpServletResponse.setHeader(Constants.AUTHORIZATION, token);
		}

	}

}

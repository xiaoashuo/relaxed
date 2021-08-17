package com.relaxed.common.security.jwt.filter;

import com.relaxed.common.security.jwt.core.Constants;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.log.LogMessage;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yakir
 * @Topic JwtAuthenticationTokenFilter
 * @Description
 * @date 2021/8/11 20:34
 * @Version 1.0
 */
@Slf4j
@Data
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

	private AuthenticationManager authenticationManager;

	private AuthenticationEntryPoint authenticationEntryPoint;

	private AuthenticationConverter authenticationConverter;

	private AuthenticationSuccessHandler successHandler;

	private AuthenticationFailureHandler failureHandler;

	private List<RequestMatcher> ignoreRequest;

	private RequestMatcher requiresAuthenticationRequestMatcher = new RequestHeaderRequestMatcher(
			Constants.AUTHORIZATION);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// 若不带 请求认证头 则直接放行
		if (!requiresAuthentication(request, response)) {
			filterChain.doFilter(request, response);
			return;
		}
		// 若带请求认证头 则判断是否在忽略列表 存在则 跳过
		if (ignoreRequest(request)) {
			filterChain.doFilter(request, response);
			return;
		}
		// 封装成jwtToken 未认证状态的
		Authentication authResult = authenticationConverter.convert(request);
		if (authResult == null) {
			this.logger.trace(
					"Did not process authentication request since failed to find jwt token in jwt Authorization header");
			filterChain.doFilter(request, response);
			return;
		}
		try {
			// 认证后的 已经填充状态

			authResult = this.getAuthenticationManager().authenticate(authResult);
			successfulAuthentication(request, response, filterChain, authResult);

		}
		catch (AuthenticationException e) {
			this.logger.debug("Failed to process authentication request", e);
			unsuccessfulAuthentication(request, response, e);
			return;
		}
		filterChain.doFilter(request, response);

	}

	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		SecurityContextHolder.clearContext();
		failureHandler.onAuthenticationFailure(request, response, failed);
		this.authenticationEntryPoint.commence(request, response, failed);

	}

	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		SecurityContextHolder.getContext().setAuthentication(authResult);
		if (this.logger.isDebugEnabled()) {
			this.logger.debug(LogMessage.format("Set SecurityContextHolder to %s", authResult));
		}
		successHandler.onAuthenticationSuccess(request, response, authResult);

	}

	protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
		return requiresAuthenticationRequestMatcher.matches(request);
	}

	public void setAuthenticationConverter(AuthenticationConverter authenticationConverter) {
		this.authenticationConverter = authenticationConverter;
	}

	public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
		Assert.notNull(successHandler, "successHandler cannot be null");
		this.successHandler = successHandler;
	}

	public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
		Assert.notNull(failureHandler, "failureHandler cannot be null");
		this.failureHandler = failureHandler;
	}

	protected AuthenticationSuccessHandler getSuccessHandler() {
		return this.successHandler;
	}

	protected AuthenticationFailureHandler getFailureHandler() {

		return this.failureHandler;
	}

	/**
	 * 当前过滤器 是否忽略请求
	 * @author yakir
	 * @date 2021/8/17 18:28
	 * @param request
	 * @return boolean
	 */
	protected boolean ignoreRequest(HttpServletRequest request) {
		if (ignoreRequest == null) {
			return false;
		}
		for (RequestMatcher permissiveMatcher : ignoreRequest) {
			if (permissiveMatcher.matches(request)) {
				return true;
			}
		}
		return false;
	}

}

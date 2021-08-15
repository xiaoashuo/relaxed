package com.relaxed.common.security.jwt.config;

import com.relaxed.common.security.jwt.filter.JwtAuthenticationEntryPoint;
import com.relaxed.common.security.jwt.filter.JwtAuthenticationConverter;
import com.relaxed.common.security.jwt.filter.JwtAuthenticationTokenFilter;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * @author Yakir
 * @Topic JwtConfigurer
 * @Description
 * @date 2021/8/15 10:32
 * @Version 1.0
 */
public class JwtConfigurer<T extends JwtConfigurer<T, B>, B extends HttpSecurityBuilder<B>>
		extends AbstractHttpConfigurer<T, B> {

	private static final RequestHeaderRequestMatcher X_REQUESTED_WITH = new RequestHeaderRequestMatcher(
			"X-Requested-With", "XMLHttpRequest");

	private static final String JWT_REALM = "Jwt_Realm";

	private AuthenticationEntryPoint authenticationEntryPoint;

	private AuthenticationConverter authenticationConverter;

	private AuthenticationSuccessHandler successHandler;

	private AuthenticationFailureHandler failureHandler;

	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint = new JwtAuthenticationEntryPoint();

	private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

	public JwtConfigurer() {
		this.realmName(JWT_REALM);
		this.jwtAuthenticationTokenFilter = new JwtAuthenticationTokenFilter();
		this.authenticationEntryPoint = jwtAuthenticationEntryPoint;
		this.authenticationConverter = new JwtAuthenticationConverter();
		this.failureHandler = (httpServletRequest, httpServletResponse, e) -> {

		};
		this.successHandler = (httpServletRequest, httpServletResponse, authentication) -> {

		};
	}

	public JwtConfigurer<T, B> realmName(String realmName) {
		this.jwtAuthenticationEntryPoint.setRealmName(realmName);
		this.jwtAuthenticationEntryPoint.afterPropertiesSet();
		return this;
	}

	public JwtConfigurer<T, B> successHandlerRegister(AuthenticationSuccessHandler successHandler) {
		this.successHandler = successHandler;
		return this;
	}

	public JwtConfigurer<T, B> failureHandlerRegister(AuthenticationFailureHandler failureHandler) {
		this.failureHandler = failureHandler;
		return this;
	}

	public JwtConfigurer<T, B> authenticationConvert(AuthenticationConverter authenticationConverter) {
		this.authenticationConverter = authenticationConverter;
		return this;
	}

	public JwtConfigurer<T, B> authenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
		this.authenticationEntryPoint = authenticationEntryPoint;
		return this;
	}

	@Override
	public void configure(B http) throws Exception {
		AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
		jwtAuthenticationTokenFilter.setAuthenticationManager(authenticationManager);
		jwtAuthenticationTokenFilter.setAuthenticationEntryPoint(authenticationEntryPoint);
		jwtAuthenticationTokenFilter.setAuthenticationConverter(authenticationConverter);
		jwtAuthenticationTokenFilter.setAuthenticationSuccessHandler(successHandler);
		jwtAuthenticationTokenFilter.setAuthenticationFailureHandler(failureHandler);
		// 将filter放到logoutFilter之前
		JwtAuthenticationTokenFilter filter = this.postProcess(jwtAuthenticationTokenFilter);
		http.addFilterBefore(filter, LogoutFilter.class);

	}

}

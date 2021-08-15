package com.relaxed.common.security.jwt.filter;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Yakir
 * @Topic JwtAuthenticationEntryPoint
 * @Description
 * @date 2021/8/15 10:44
 * @Version 1.0
 */
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, InitializingBean {

	private String realmName;

	@Override
	public void afterPropertiesSet() {
		Assert.hasText(this.realmName, "realmName must be specified");
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
	}

	public String getRealmName() {
		return this.realmName;
	}

	public void setRealmName(String realmName) {
		this.realmName = realmName;
	}

}

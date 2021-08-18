package com.relaxed.common.security.jwt.handler;

import cn.hutool.json.JSONUtil;
import com.relaxed.common.core.util.ServletUtils;
import com.relaxed.common.model.result.R;
import com.relaxed.common.security.jwt.core.Constants;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yakir
 * @Topic FormLoginSuceessHandler
 * @Description
 * @date 2021/8/15 9:31
 * @Version 1.0
 */
@RequiredArgsConstructor
public class FormLoginSuccessHandler implements AuthenticationSuccessHandler {

	private final JwtTokenService jwtTokenService;

	private static final String ACCESS_TOKEN = "access_token";

	@Override
	public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Authentication authentication) {
		String token = jwtTokenService.generateToken((UserDetails) authentication.getPrincipal());
		httpServletResponse.setHeader(Constants.AUTHORIZATION, token);
		Map<String, String> maps = new HashMap<>();
		maps.put(ACCESS_TOKEN, token);
		ServletUtils.renderString(httpServletResponse, JSONUtil.toJsonStr(R.ok(maps)));
	}

}

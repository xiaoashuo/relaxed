package com.relaxed.oauth2.common.handler;

import cn.hutool.json.JSONUtil;
import com.relaxed.common.model.result.R;
import com.relaxed.common.model.result.SysResultCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Yakir
 * @Topic CustomAuthenticationEntryPoint
 * @Description
 * @date 2022/7/22 16:00
 * @Version 1.0
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		String utf8 = StandardCharsets.UTF_8.toString();
		response.setHeader("Content-Type", "application/json");
		response.setHeader("Accept-Charset", utf8);
		response.setCharacterEncoding(utf8);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		R<Object> r = R.failed(SysResultCode.UNAUTHORIZED, authException.getMessage());
		response.getWriter().write(JSONUtil.toJsonStr(r));
	}

}

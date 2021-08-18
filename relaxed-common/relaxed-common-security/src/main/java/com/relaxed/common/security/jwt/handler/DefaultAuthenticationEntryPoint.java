package com.relaxed.common.security.jwt.handler;

import cn.hutool.json.JSONUtil;
import com.relaxed.common.core.util.ServletUtils;
import com.relaxed.common.model.result.R;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Yakir
 * @Topic DefaultAuthenticationEntryPoint
 * @Description
 * @date 2021/8/18 15:53
 * @Version 1.0
 */
public class DefaultAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		ServletUtils.renderString(response,
				JSONUtil.toJsonStr(R.failed(HttpStatus.UNAUTHORIZED.value(), authException.getMessage())));
	}

}

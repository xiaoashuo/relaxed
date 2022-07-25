package com.relaxed.common.resource.handler;

import cn.hutool.json.JSONUtil;
import com.relaxed.common.model.result.R;
import com.relaxed.common.model.result.SysResultCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Yakir
 * @Topic CustomAccessDeniedHandler
 * @Description 当拒绝访问时返回消息
 * @date 2022/7/25 14:36
 * @Version 1.0
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		String utf8 = StandardCharsets.UTF_8.toString();
		response.setHeader("Content-Type", "application/json");
		response.setHeader("Accept-Charset", utf8);
		response.setCharacterEncoding(utf8);
		response.setStatus(HttpStatus.FORBIDDEN.value());
		R<Object> r = R.failed(SysResultCode.FORBIDDEN, accessDeniedException.getMessage());
		response.getWriter().write(JSONUtil.toJsonStr(r));
	}

}

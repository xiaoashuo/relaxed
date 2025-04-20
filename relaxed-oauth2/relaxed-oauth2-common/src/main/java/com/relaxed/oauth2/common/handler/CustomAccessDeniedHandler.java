package com.relaxed.oauth2.common.handler;

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
 * 自定义访问拒绝处理器 用于处理无权限访问的请求，返回统一的JSON格式响应 当用户访问需要特定权限的资源但权限不足时触发
 *
 * @author Yakir
 * @since 1.0
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	/**
	 * 处理无权限访问的请求 设置响应头信息，返回403状态码和错误信息
	 * @param request HTTP请求
	 * @param response HTTP响应
	 * @param accessDeniedException 访问拒绝异常
	 * @throws IOException IO异常
	 * @throws ServletException Servlet异常
	 */
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		// 设置响应字符集为UTF-8
		String utf8 = StandardCharsets.UTF_8.toString();
		response.setHeader("Content-Type", "application/json");
		response.setHeader("Accept-Charset", utf8);
		response.setCharacterEncoding(utf8);

		// 设置HTTP状态码为403禁止访问
		response.setStatus(HttpStatus.FORBIDDEN.value());

		// 构建统一的错误响应
		R<Object> r = R.failed(SysResultCode.FORBIDDEN, accessDeniedException.getMessage());
		response.getWriter().write(JSONUtil.toJsonStr(r));
	}

}

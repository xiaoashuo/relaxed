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
 * 自定义认证入口点 用于处理未认证的请求，返回统一的JSON格式响应 当用户访问需要认证的资源但未提供有效凭证时触发
 *
 * @author Yakir
 * @since 1.0
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	/**
	 * 处理未认证的请求 设置响应头信息，返回401状态码和错误信息
	 * @param request HTTP请求
	 * @param response HTTP响应
	 * @param authException 认证异常
	 * @throws IOException IO异常
	 * @throws ServletException Servlet异常
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		// 设置响应字符集为UTF-8
		String utf8 = StandardCharsets.UTF_8.toString();
		response.setHeader("Content-Type", "application/json");
		response.setHeader("Accept-Charset", utf8);
		response.setCharacterEncoding(utf8);

		// 设置HTTP状态码为401未授权
		response.setStatus(HttpStatus.UNAUTHORIZED.value());

		// 构建统一的错误响应
		R<Object> r = R.failed(SysResultCode.UNAUTHORIZED, authException.getMessage());
		response.getWriter().write(JSONUtil.toJsonStr(r));
	}

}

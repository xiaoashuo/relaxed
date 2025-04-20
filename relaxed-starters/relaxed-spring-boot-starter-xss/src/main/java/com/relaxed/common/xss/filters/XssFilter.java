package com.relaxed.common.xss.filters;

import cn.hutool.core.util.StrUtil;
import com.relaxed.common.xss.config.XssProperties;
import com.relaxed.common.xss.servlet.XssRequestWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * XSS过滤器 用于过滤HTTP请求中的XSS攻击内容 继承自OncePerRequestFilter，确保每个请求只被过滤一次
 *
 * @author Yakir
 * @since 1.0
 */
@RequiredArgsConstructor
public class XssFilter extends OncePerRequestFilter {

	private final XssProperties xssProperties;

	/**
	 * 执行XSS过滤 使用XssRequestWrapper包装请求对象，对请求参数进行XSS过滤
	 * @param request HTTP请求对象
	 * @param response HTTP响应对象
	 * @param filterChain 过滤器链
	 * @throws ServletException 如果处理请求时发生异常
	 * @throws IOException 如果处理IO时发生异常
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		filterChain.doFilter(new XssRequestWrapper(request), response);
	}

	/**
	 * 判断当前请求是否应该跳过XSS过滤
	 * @param request HTTP请求对象
	 * @return true表示跳过过滤，false表示需要过滤
	 * @throws ServletException 如果判断过程中发生异常
	 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return xssProperties.shouldNotFilter(request);
	}

}

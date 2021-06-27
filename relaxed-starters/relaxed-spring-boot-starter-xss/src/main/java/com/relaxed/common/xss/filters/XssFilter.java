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
 * @author Yakir
 * @Topic XssRequestFilter
 * @Description
 * @date 2021/6/26 20:58
 * @Version 1.0
 */
@RequiredArgsConstructor
public class XssFilter extends OncePerRequestFilter {

	private final XssProperties xssProperties;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		filterChain.doFilter(new XssRequestWrapper(request), response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return xssProperties.shouldNotFilter(request);
	}

}

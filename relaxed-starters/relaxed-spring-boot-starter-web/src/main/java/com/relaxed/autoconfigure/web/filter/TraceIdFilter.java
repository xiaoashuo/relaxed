package com.relaxed.autoconfigure.web.filter;

import cn.hutool.core.util.IdUtil;
import com.relaxed.autoconfigure.web.constants.LogConstant;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TraceId过滤器 利用Slf4J的MDC功能，为每个请求添加唯一的TraceId 用于在分布式系统中追踪请求链路
 *
 * @author Yakir
 * @since 1.0
 */
public class TraceIdFilter extends OncePerRequestFilter {

	/**
	 * 处理HTTP请求 为每个请求添加唯一的TraceId，并在请求完成后清理
	 * @param request HTTP请求
	 * @param response HTTP响应
	 * @param filterChain 过滤器链
	 * @throws ServletException 如果处理请求时发生异常
	 * @throws IOException 如果处理IO时发生异常
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		MDC.put(LogConstant.TRACE_ID, IdUtil.objectId());
		try {
			filterChain.doFilter(request, response);
		}
		finally {
			MDC.remove(LogConstant.TRACE_ID);
		}
	}

}

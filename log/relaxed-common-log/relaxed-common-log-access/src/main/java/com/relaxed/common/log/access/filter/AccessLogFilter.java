package com.relaxed.common.log.access.filter;

import cn.hutool.core.collection.CollectionUtil;
import com.relaxed.common.core.request.RepeatBodyRequestWrapper;
import com.relaxed.common.log.access.handler.AccessLogHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.ServletRequestPathUtils;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 访问日志过滤器类。 用于拦截HTTP请求并记录访问日志。 主要功能包括： 1. 根据URL规则匹配决定是否记录日志 2. 包装请求和响应以支持重复读取 3.
 * 记录请求处理时间和异常信息 4. 支持自定义日志处理器
 *
 * @author Yakir
 * @since 1.0
 */
@Slf4j
public class AccessLogFilter extends OncePerRequestFilter {

	/**
	 * Ant风格路径匹配器 用于匹配URL规则
	 */
	private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

	/**
	 * URL路径匹配帮助类 用于解析请求路径
	 */
	private static final UrlPathHelper URL_PATH_HELPER = new UrlPathHelper();

	/**
	 * 访问日志配置属性
	 */
	private final LogAccessProperties logAccessProperties;

	/**
	 * 访问日志处理器
	 */
	private final AccessLogHandler accessLogHandler;

	/**
	 * 默认的访问日志规则
	 */
	private final LogAccessRule defaultLogAccessRule;

	/**
	 * 创建访问日志过滤器实例
	 * @param logAccessProperties 访问日志配置属性
	 * @param accessLogHandler 访问日志处理器
	 * @param defaultLogAccessRule 默认的访问日志规则
	 */
	public AccessLogFilter(LogAccessProperties logAccessProperties, AccessLogHandler accessLogHandler,
			LogAccessRule defaultLogAccessRule) {
		this.logAccessProperties = logAccessProperties;
		this.accessLogHandler = accessLogHandler;
		this.defaultLogAccessRule = defaultLogAccessRule;
	}

	/**
	 * 处理HTTP请求并记录访问日志
	 * @param request HTTP请求
	 * @param response HTTP响应
	 * @param filterChain 过滤器链
	 * @throws ServletException 如果发生Servlet异常
	 * @throws IOException 如果发生IO异常
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// 不需要记录 直接跳出
		if (!accessLogHandler.shouldLog(request)) {
			filterChain.doFilter(request, response);
			return;
		}
		// 获取请求规则
		LogAccessRule logAccessRule = searchRequestRule(request);
		LogAccessRule.RecordOption recordOption = logAccessRule.getRecordOption();
		// 若当前url匹配规则 为忽略 则跳过
		if (recordOption.isIgnore()) {
			filterChain.doFilter(request, response);
			return;
		}
		// 重复使用请求
		HttpServletRequest requestToUse = recordOption.isIncludeRequest() ? wrapperRequest(request) : request;

		// 包装响应
		HttpServletResponse responseToUse = recordOption.isIncludeResponse() ? wrapperResponse(response) : response;
		// 开始时间
		Long startTime = System.currentTimeMillis();
		Throwable myThrowable = null;
		Object buildParam = null;
		// 请求前处理
		try {
			buildParam = accessLogHandler.beforeRequest(requestToUse, logAccessRule);
		}
		catch (Exception e) {
			this.logger.error("[Access Log] process before request error", e);
		}
		try {
			filterChain.doFilter(requestToUse, responseToUse);
		}
		catch (Throwable throwable) {
			// 记录外抛异常
			myThrowable = throwable;
			throw throwable;
		}
		finally {
			// 结束时间
			Long endTime = System.currentTimeMillis();
			// 执行时长
			Long executionTime = endTime - startTime;
			// 记录在doFilter里被程序处理过后的异常，可参考
			// http://www.runoob.com/servlet/servlet-exception-handling.html
			Throwable throwable = (Throwable) requestToUse.getAttribute("javax.servlet.error.exception");
			if (throwable != null) {
				myThrowable = throwable;
			}
			// 生产一个日志并记录
			try {
				accessLogHandler.afterRequest(buildParam, requestToUse, responseToUse, executionTime, myThrowable,
						logAccessRule);
			}
			catch (Exception e) {
				this.logger.error("[Access Log] process after request error, handler: %s", e);
			}
			// 重新写入数据到响应信息中
			if (responseToUse instanceof ContentCachingResponseWrapper) {
				((ContentCachingResponseWrapper) responseToUse).copyBodyToResponse();
			}
		}

	}

	/**
	 * 包装HTTP响应 用于支持重复读取响应内容
	 * @param response 原始HTTP响应
	 * @return 包装后的HTTP响应
	 */
	protected HttpServletResponse wrapperResponse(HttpServletResponse response) {
		// 包装 response，便于重复获取 body
		return new ContentCachingResponseWrapper(response);
	}

	/**
	 * 包装HTTP请求 用于支持重复读取请求内容
	 * @param request 原始HTTP请求
	 * @return 包装后的HTTP请求
	 */
	protected HttpServletRequest wrapperRequest(HttpServletRequest request) {
		// 避免由于路由信息缺失，导致无法获取到请求目标的执行方法
		if (!ServletRequestPathUtils.hasParsedRequestPath(request)) {
			ServletRequestPathUtils.parseAndCache(request);
		}

		// 包装 request，以保证可以重复读取body
		// spring 提供的 ContentCachingRequestWrapper，在 body 没有被程序使用时，获取到的 body 缓存为空
		// 且对于 form data，会混淆 payload 和 query string
		return new RepeatBodyRequestWrapper(request);
	}

	/**
	 * 根据请求查找匹配的日志规则
	 * @param request HTTP请求
	 * @return 匹配的日志规则，如果没有匹配则返回默认规则
	 */
	protected LogAccessRule searchRequestRule(HttpServletRequest request) {
		List<LogAccessRule> urlRules = logAccessProperties.getUrlRules();
		if (CollectionUtil.isEmpty(urlRules)) {
			return this.defaultLogAccessRule;
		}
		String lookupPathForRequest = URL_PATH_HELPER.getLookupPathForRequest(request);
		for (LogAccessRule urlRule : urlRules) {
			if (ANT_PATH_MATCHER.match(urlRule.getUrlPattern(), lookupPathForRequest)) {
				return urlRule;
			}
		}
		return this.defaultLogAccessRule;
	}

	/**
	 * 获取访问日志处理器
	 * @return 访问日志处理器
	 */
	protected AccessLogHandler getAccessLogHandler() {
		return accessLogHandler;
	}

	/**
	 * 获取访问日志配置属性
	 * @return 访问日志配置属性
	 */
	protected LogAccessProperties getLogAccessProperties() {
		return logAccessProperties;
	}

	/**
	 * 获取默认的访问日志规则
	 * @return 默认的访问日志规则
	 */
	protected LogAccessRule getDefaultLogAccessRule() {
		return defaultLogAccessRule;
	}

	/**
	 * 获取Ant风格路径匹配器
	 * @return Ant风格路径匹配器
	 */
	protected AntPathMatcher getAntPathMatcher() {
		return ANT_PATH_MATCHER;
	}

	/**
	 * 获取URL路径匹配帮助类
	 * @return URL路径匹配帮助类
	 */
	protected UrlPathHelper getUrlPathHelper() {
		return URL_PATH_HELPER;
	}

}

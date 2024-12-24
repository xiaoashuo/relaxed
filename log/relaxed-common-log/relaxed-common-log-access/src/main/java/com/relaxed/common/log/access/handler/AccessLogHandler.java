package com.relaxed.common.log.access.handler;

import com.relaxed.common.log.access.filter.LogAccessProperties;
import com.relaxed.common.log.access.filter.LogAccessRule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/15 22:21
 */
public interface AccessLogHandler<T> {

	/**
	 * 请求前处理
	 * @param request
	 * @param logAccessRule
	 * @return 构建请求实体信息
	 */
	T beforeRequest(HttpServletRequest request, LogAccessRule logAccessRule);

	/**
	 * 请求后处理
	 * @param buildParam 请求方法执行前构建的参数
	 * @param request 请求
	 * @param response 响应
	 * @param executionTime 执行时间
	 * @param myThrowable 异常
	 * @param logAccessRule 访问日志规则
	 */
	void afterRequest(T buildParam, HttpServletRequest request, HttpServletResponse response, Long executionTime,
			Throwable myThrowable, LogAccessRule logAccessRule);

	/**
	 * 是否应该记录日志 可以过滤一些不需要记录日志的请求
	 * @param request
	 * @return true 记录 false 不记录
	 */
	default boolean shouldLog(HttpServletRequest request) {
		return true;
	}

}

package com.relaxed.common.log.access.handler;

import com.relaxed.common.log.access.filter.LogAccessProperties;
import com.relaxed.common.log.access.filter.LogAccessRule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 访问日志处理器接口。 用于处理访问日志的记录逻辑。 主要功能包括： 1. 请求前处理，构建请求实体信息 2. 请求后处理，记录完整的访问日志 3. 控制是否记录日志
 *
 * @param <T> 请求实体类型
 * @author Yakir
 * @since 1.0
 */
public interface AccessLogHandler<T> {

	/**
	 * 请求前处理。 在请求处理前执行，用于构建请求实体信息。
	 * @param request HTTP请求
	 * @param logAccessRule 访问日志规则
	 * @return 构建的请求实体信息
	 */
	T beforeRequest(HttpServletRequest request, LogAccessRule logAccessRule);

	/**
	 * 请求后处理。 在请求处理后执行，用于记录完整的访问日志。
	 * @param buildParam 请求前构建的参数
	 * @param request HTTP请求
	 * @param response HTTP响应
	 * @param executionTime 请求执行时间（毫秒）
	 * @param myThrowable 请求处理过程中的异常
	 * @param logAccessRule 访问日志规则
	 */
	void afterRequest(T buildParam, HttpServletRequest request, HttpServletResponse response, Long executionTime,
			Throwable myThrowable, LogAccessRule logAccessRule);

	/**
	 * 判断是否应该记录日志。 可以用于过滤一些不需要记录日志的请求。
	 * @param request HTTP请求
	 * @return true 表示需要记录日志，false 表示不需要记录日志
	 */
	default boolean shouldLog(HttpServletRequest request) {
		return true;
	}

}

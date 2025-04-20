package com.relaxed.autoconfigure.web.constants;

/**
 * 日志常量类 定义日志相关的常量，如TraceId等
 *
 * @author Yakir
 * @since 1.0
 */
public final class LogConstant {

	/**
	 * 私有构造函数，防止实例化
	 */
	private LogConstant() {
	}

	/**
	 * 跟踪ID常量 用于在一次请求或执行方法时，关联产生的各种日志
	 */
	public static final String TRACE_ID = "traceId";

}

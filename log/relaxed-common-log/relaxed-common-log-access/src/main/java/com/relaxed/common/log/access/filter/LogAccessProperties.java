package com.relaxed.common.log.access.filter;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 访问日志配置属性类。 用于配置访问日志过滤器的相关属性。 主要功能包括： 1. 控制访问日志的启用状态 2. 配置过滤器的执行顺序 3. 配置URL匹配规则
 *
 * @author Yakir
 * @since 1.0
 */
@Data
public class LogAccessProperties {

	/**
	 * 访问日志配置前缀
	 */
	public static final String PREFIX = "relaxed.log.access";

	/**
	 * 是否启用访问日志功能 默认为 false
	 */
	private boolean enabled;

	/**
	 * 访问日志过滤器的执行顺序 值越小优先级越高 默认为 -10
	 */
	private int order = -10;

	/**
	 * URL匹配规则列表 用于配置需要记录访问日志的URL模式 默认为空列表
	 */
	private List<LogAccessRule> urlRules = new ArrayList<>();

}

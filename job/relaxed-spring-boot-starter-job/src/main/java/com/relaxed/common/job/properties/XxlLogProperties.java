package com.relaxed.common.job.properties;

import ch.qos.logback.classic.Level;
import lombok.Data;

/**
 * XXL-Job 日志配置属性类。 用于配置 XXL-Job 的日志同步功能，主要功能包括： 1. 控制日志同步开关 2. 配置日志名称和前缀 3. 设置包过滤规则 4.
 * 配置日志级别
 *
 * @author Yakir
 * @since 1.0
 */
@Data
public class XxlLogProperties {

	/**
	 * 是否启用日志同步功能
	 */
	private boolean enabled = true;

	/**
	 * 日志名称
	 */
	private String name = "logXXl";

	/**
	 * 日志前缀
	 */
	private String logPrefix = "XXL-";

	/**
	 * 需要同步的包路径
	 */
	private String[] includePackages;

	/**
	 * 不需要同步的包路径
	 */
	private String[] excludePackages;

	/**
	 * 最小日志级别
	 */
	private Level minLevel = Level.INFO;

}

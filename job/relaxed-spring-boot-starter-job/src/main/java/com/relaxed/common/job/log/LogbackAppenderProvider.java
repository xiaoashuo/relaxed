package com.relaxed.common.job.log;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.relaxed.common.job.properties.XxlLogProperties;

/**
 * Logback 日志追加器提供者接口。 用于为 XXL-Job 提供自定义的日志追加器实现。 主要功能包括： 1. 添加日志追加器到日志上下文 2.
 * 提供自定义的日志追加器实现
 *
 * @author Yakir
 * @since 1.0
 */
public interface LogbackAppenderProvider {

	/**
	 * 添加日志追加器到日志上下文。
	 * @param xxlLogProperties XXL-Job 日志配置属性
	 */
	void addAppender(XxlLogProperties xxlLogProperties);

	/**
	 * 提供自定义的日志追加器实现。
	 * @param loggerContext 日志上下文
	 * @param xxlLogProperties XXL-Job 日志配置属性
	 * @return 日志追加器实例
	 */
	Appender<ILoggingEvent> provider(LoggerContext loggerContext, XxlLogProperties xxlLogProperties);

}

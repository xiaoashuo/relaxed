package com.relaxed.common.job.log;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.relaxed.common.job.properties.XxlLogProperties;
import org.slf4j.LoggerFactory;

/**
 * 默认的 Logback 日志追加器提供者实现类。 实现了 LogbackAppenderProvider 接口，提供以下功能： 1. 将日志追加器添加到根日志记录器 2.
 * 创建并配置 XxlJobLogAppender 3. 支持包过滤和日志级别控制
 *
 * @author Yakir
 * @since 1.0
 */
public class DefaultLogBackAppenderProvider implements LogbackAppenderProvider {

	/**
	 * 添加日志追加器到日志上下文。 将创建的日志追加器添加到根日志记录器。
	 * @param logProperties XXL-Job 日志配置属性
	 */
	@Override
	public void addAppender(XxlLogProperties logProperties) {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		Appender<ILoggingEvent> logAppender = this.provider(loggerContext, logProperties);
		// 获取根日志记录器并将 appender 添加到根日志记录器
		Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
		rootLogger.addAppender(logAppender);
		//
		// Map<String, LoggerConfig> loggers = configuration.getLoggers();
		// Iterator<Map.Entry<String, LoggerConfig>> iterator =
		// loggers.entrySet().iterator();
		// while (iterator.hasNext()) {
		// Map.Entry<String, LoggerConfig> next = iterator.next();
		// //给logger关联 appender
		// next.getValue().addAppender(rollingFileAppender, null, null);
		// }
		// 更新 loggerContext
		// loggerContext.updateLoggers(configuration);
	}

	/**
	 * 提供自定义的日志追加器实现。 创建并配置 XxlJobLogAppender 实例。
	 * @param loggerContext 日志上下文
	 * @param xxlLogProperties XXL-Job 日志配置属性
	 * @return 配置好的日志追加器实例
	 */
	@Override
	public Appender<ILoggingEvent> provider(LoggerContext loggerContext, XxlLogProperties xxlLogProperties) {
		XxlJobLogAppender xxlJobLogAppender = new XxlJobLogAppender();
		xxlJobLogAppender.setLogPrefix(xxlLogProperties.getLogPrefix());
		xxlJobLogAppender.setIncludePackages(xxlLogProperties.getIncludePackages());
		xxlJobLogAppender.setExcludePackages(xxlLogProperties.getExcludePackages());
		xxlJobLogAppender.setMinLevel(xxlLogProperties.getMinLevel());
		xxlJobLogAppender.setName(xxlLogProperties.getName());
		// 设置 appender 编码器
		// Encoder encoder = new ch.qos.logback.classic.encoder.PatternLayoutEncoder();
		// ((ch.qos.logback.classic.encoder.PatternLayoutEncoder)
		// encoder).setPattern("%d{yyyy-MM-dd HH:mm:ss} - %msg%n");
		// encoder.setContext(loggerContext);
		// encoder.start();

		// xxlJobLogAppender.setEncoder(encoder);
		xxlJobLogAppender.setContext(loggerContext);
		xxlJobLogAppender.start();
		return xxlJobLogAppender;
	}

}

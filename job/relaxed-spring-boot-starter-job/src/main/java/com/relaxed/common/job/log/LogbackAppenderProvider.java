package com.relaxed.common.job.log;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.relaxed.common.job.properties.XxlLogProperties;

/**
 * @author Yakir
 * @Topic LogbackAppenderProvider
 * @Description
 * @date 2024/12/20 17:44
 * @Version 1.0
 */
public interface LogbackAppenderProvider {

	void addAppender(XxlLogProperties xxlLogProperties);

	Appender<ILoggingEvent> provider(LoggerContext loggerContext, XxlLogProperties xxlLogProperties);

}

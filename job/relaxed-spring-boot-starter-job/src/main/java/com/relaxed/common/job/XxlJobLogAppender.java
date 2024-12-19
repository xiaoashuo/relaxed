package com.relaxed.common.job;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.AppenderBase;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

/**
 * @author Yakir
 * @Topic XxlJobLogAppender
 * @Description
 * @date 2024/12/18 15:59
 * @Version 1.0
 */
@Setter
public class XxlJobLogAppender extends AppenderBase<ILoggingEvent> {

	private String logPrefix = "XXL-";

	private String[] includePackages;

	private String[] excludePackages;

	private Level minLevel = Level.INFO;

	@Override
	protected void append(ILoggingEvent event) {
		try {
			// 检查日志级别
			if (event.getLevel().isGreaterOrEqual(minLevel)) {
				String loggerName = event.getLoggerName();
				String message = event.getFormattedMessage();

				// 检查包名过滤
				if (isPackageIncluded(loggerName) && !isPackageExcluded(loggerName)) {
					// 检查前缀或自定义标记
					if (message != null && (message.startsWith(logPrefix) || hasCustomMarker(event))) {

						// 构建完整日志信息
						StringBuilder fullMessage = new StringBuilder();

						// 添加时间戳
						fullMessage.append("[").append(
								new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(event.getTimeStamp())))
								.append("] ");

						// 添加日志级别
						fullMessage.append("[").append(event.getLevel()).append("] ");
						// 添加traceId
						String traceId = Optional.ofNullable(event.getMDCPropertyMap()).map(item -> item.get("traceId"))
								.orElse("");
						if (StringUtils.hasText(traceId)) {
							fullMessage.append("[").append(traceId).append("] ");
						}
						// 添加日志内容
						fullMessage.append(
								message.startsWith(logPrefix) ? message.substring(logPrefix.length()) : message);

						// 如果有异常堆栈，也添加进去
						if (event.getThrowableProxy() != null) {
							fullMessage.append("\n").append(getThrowableString(event.getThrowableProxy()));
						}

						// 输出到XXL-JOB日志
						XxlJobLogger.log(fullMessage.toString());
					}
				}
			}
		}
		catch (Exception e) {
			addError("Failed to append log to XXL-JOB", e);
		}
	}

	private boolean isPackageIncluded(String loggerName) {
		if (includePackages == null || includePackages.length == 0) {
			return true;
		}
		for (String pkg : includePackages) {
			if (loggerName.startsWith(pkg)) {
				return true;
			}
		}
		return false;
	}

	private boolean isPackageExcluded(String loggerName) {
		if (excludePackages == null || excludePackages.length == 0) {
			return false;
		}
		for (String pkg : excludePackages) {
			if (loggerName.startsWith(pkg)) {
				return true;
			}
		}
		return false;
	}

	private boolean hasCustomMarker(ILoggingEvent event) {
		// 检查是否有自定义Marker
		return event.getMarker() != null && "XXL_JOB".equals(event.getMarker().getName());
	}

	private String getThrowableString(IThrowableProxy throwableProxy) {
		StringBuilder sb = new StringBuilder();
		sb.append(throwableProxy.getClassName()).append(": ").append(throwableProxy.getMessage()).append("\n");

		for (StackTraceElementProxy step : throwableProxy.getStackTraceElementProxyArray()) {
			sb.append("\tat ").append(step.toString()).append("\n");
		}
		return sb.toString();
	}

}

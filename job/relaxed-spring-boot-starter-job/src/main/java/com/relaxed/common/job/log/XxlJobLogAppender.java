package com.relaxed.common.job.log;

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
 * XXL-Job 日志追加器。 继承自 AppenderBase，用于将日志输出到 XXL-Job 的日志系统中。 主要功能包括： 1. 支持日志级别过滤 2. 支持包路径过滤
 * 3. 支持日志前缀过滤 4. 支持异常堆栈输出 5. 支持 traceId 追踪
 *
 * @author Yakir
 */
@Setter
public class XxlJobLogAppender extends AppenderBase<ILoggingEvent> {

	/**
	 * 日志前缀，用于过滤需要同步到 XXL-Job 的日志
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

	/**
	 * 追加日志事件。 根据配置的过滤规则，将符合条件的日志输出到 XXL-Job 日志系统。
	 * @param event 日志事件
	 */
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

						// 添加时间戳 默认xxl会输出
						// fullMessage.append("[").append(
						// new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new
						// Date(event.getTimeStamp())))
						// .append("] ");

						// 添加日志级别
						// fullMessage.append("[").append(event.getLevel()).append("] ");
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

	/**
	 * 检查日志包路径是否在包含列表中。
	 * @param loggerName 日志记录器名称
	 * @return 如果包含则返回 true，否则返回 false
	 */
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

	/**
	 * 检查日志包路径是否在排除列表中。
	 * @param loggerName 日志记录器名称
	 * @return 如果排除则返回 true，否则返回 false
	 */
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

	/**
	 * 检查日志事件是否有自定义标记。
	 * @param event 日志事件
	 * @return 如果有自定义标记则返回 true，否则返回 false
	 */
	private boolean hasCustomMarker(ILoggingEvent event) {
		// 检查是否有自定义Marker
		return event.getMarker() != null && "XXL_JOB".equals(event.getMarker().getName());
	}

	/**
	 * 获取异常堆栈的字符串表示。
	 * @param throwableProxy 异常代理对象
	 * @return 异常堆栈的字符串表示
	 */
	private String getThrowableString(IThrowableProxy throwableProxy) {
		StringBuilder sb = new StringBuilder();
		sb.append(throwableProxy.getClassName()).append(": ").append(throwableProxy.getMessage()).append("\n");

		for (StackTraceElementProxy step : throwableProxy.getStackTraceElementProxyArray()) {
			sb.append("\tat ").append(step.toString()).append("\n");
		}
		return sb.toString();
	}

}

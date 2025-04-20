package com.relaxed.autoconfigure.log;

import com.relaxed.autoconfigure.log.properties.LogProperties;
import com.relaxed.common.log.access.filter.AccessLogFilter;
import com.relaxed.common.log.access.filter.LogAccessProperties;
import com.relaxed.common.log.access.filter.LogAccessRule;
import com.relaxed.common.log.access.handler.AccessLogHandler;
import com.relaxed.common.log.access.handler.DefaultAccessLogHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

/**
 * 访问日志自动配置类。 用于配置访问日志的过滤器和处理器。 主要功能包括： 1. 根据配置决定是否启用访问日志 2. 注册访问日志过滤器 3. 支持自定义访问日志处理器 4.
 * 支持默认的日志记录规则
 *
 * @author Yakir
 * @since 1.0
 */
@Configuration
@Slf4j
@ConditionalOnWebApplication
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = LogAccessProperties.PREFIX, name = "enabled", havingValue = "true",
		matchIfMissing = true)
public class AccessLogAutoConfiguration {

	/**
	 * 日志配置属性
	 */
	private final LogProperties logProperties;

	/**
	 * 注册访问日志过滤器。 当存在 AccessLogHandler 实例时使用自定义处理器，否则使用默认处理器。 默认记录所有请求的访问日志。
	 * @param accessLogHandler 访问日志处理器，可以为空
	 * @return 过滤器注册Bean
	 */
	@Bean
	@ConditionalOnClass(AccessLogHandler.class)
	public FilterRegistrationBean<AccessLogFilter> accessLogFilterRegistrationBean(
			@Nullable AccessLogHandler<?> accessLogHandler) {
		log.debug("access log 记录拦截器已开启====");

		AccessLogHandler useAccessLogHandler = accessLogHandler == null ? new DefaultAccessLogHandler()
				: accessLogHandler;
		// 默认的日志记录规则 记录所有
		LogAccessRule defaultRule = new LogAccessRule();
		defaultRule.setUrlPattern("*");

		AccessLogFilter accessLogFilter = new AccessLogFilter(logProperties.getAccess(), useAccessLogHandler,
				defaultRule);
		FilterRegistrationBean<AccessLogFilter> registrationBean = new FilterRegistrationBean<>(accessLogFilter);
		registrationBean.setOrder(logProperties.getAccess().getOrder());
		return registrationBean;
	}

}

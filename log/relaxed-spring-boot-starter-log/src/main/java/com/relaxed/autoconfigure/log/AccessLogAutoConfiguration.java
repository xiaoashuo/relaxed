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
 * @author Hccake
 * @version 1.0
 * @date 2019/10/15 18:20
 */
@Configuration
@Slf4j
@ConditionalOnWebApplication
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = LogAccessProperties.PREFIX, name = "enabled", havingValue = "true",
		matchIfMissing = true)
public class AccessLogAutoConfiguration {

	private final LogProperties logProperties;

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

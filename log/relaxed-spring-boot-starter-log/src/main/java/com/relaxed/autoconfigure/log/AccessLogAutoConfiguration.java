package com.relaxed.autoconfigure.log;

import com.relaxed.autoconfigure.log.properties.LogProperties;
import com.relaxed.common.log.access.filter.AccessLogFilter;
import com.relaxed.common.log.access.handler.AccessLogHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/15 18:20
 */
@Configuration
@Slf4j
@ConditionalOnWebApplication
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = LogProperties.Access.PREFIX, name = "enabled", havingValue = "true",
		matchIfMissing = true)
public class AccessLogAutoConfiguration {

	private final AccessLogHandler<?> accessLogHandler;

	private final LogProperties logProperties;

	@Bean
	@ConditionalOnClass(AccessLogHandler.class)
	public FilterRegistrationBean<AccessLogFilter> accessLogFilterRegistrationBean() {
		log.debug("access log 记录拦截器已开启====");
		LogProperties.Access access = logProperties.getAccess();
		FilterRegistrationBean<AccessLogFilter> registrationBean = new FilterRegistrationBean<>(
				new AccessLogFilter(accessLogHandler, access.getIgnoreUrlPatterns()));
		registrationBean.setOrder(-10);
		return registrationBean;
	}

}

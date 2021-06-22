package com.relaxed.common.conf.exception;

import com.relaxed.common.conf.exception.handler.DefaultGlobalExceptionHandler;
import com.relaxed.common.core.exception.handler.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/15 18:20
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class ExceptionHandleAutoConfiguration {

	/**
	 * 默认的日志处理器
	 * @return DefaultExceptionHandler
	 */
	@Bean
	@ConditionalOnMissingBean(GlobalExceptionHandler.class)
	public GlobalExceptionHandler defaultGlobalExceptionHandler() {
		return new DefaultGlobalExceptionHandler();
	}

}

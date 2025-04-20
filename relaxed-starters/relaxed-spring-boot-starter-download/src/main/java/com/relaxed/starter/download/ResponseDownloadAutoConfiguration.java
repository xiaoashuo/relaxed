package com.relaxed.starter.download;

import com.relaxed.starter.download.aop.ResponseDownloadReturnValueHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 响应下载自动配置类 用于配置Spring MVC的返回值处理器，支持文件下载功能
 *
 * @author Yakir
 * @since 1.0
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class ResponseDownloadAutoConfiguration {

	private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

	private final ResponseDownloadReturnValueHandler responseDownloadReturnValueHandler;

	/**
	 * 配置Spring MVC的返回值处理器 将文件下载处理器添加到处理器链的最前面
	 */
	@PostConstruct
	public void setReturnValueHandlers() {
		List<HandlerMethodReturnValueHandler> returnValueHandlers = requestMappingHandlerAdapter
				.getReturnValueHandlers();

		List<HandlerMethodReturnValueHandler> newHandlers = new ArrayList<>();
		newHandlers.add(responseDownloadReturnValueHandler);
		assert returnValueHandlers != null;
		newHandlers.addAll(returnValueHandlers);
		requestMappingHandlerAdapter.setReturnValueHandlers(newHandlers);
	}

}

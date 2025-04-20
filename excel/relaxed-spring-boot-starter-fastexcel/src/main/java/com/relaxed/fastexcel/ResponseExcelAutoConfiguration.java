package com.relaxed.fastexcel;

import com.relaxed.fastexcel.aop.DynamicNameAspect;
import com.relaxed.fastexcel.aop.RequestExcelArgumentResolver;
import com.relaxed.fastexcel.aop.ResponseExcelReturnValueHandler;
import com.relaxed.fastexcel.config.ExcelConfigProperties;
import com.relaxed.fastexcel.processor.NameProcessor;
import com.relaxed.fastexcel.processor.NameSpelExpressionProcessor;
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
 * Excel响应自动配置类 负责配置Excel相关的处理器和解析器,包括: 1. Excel名称解析处理器 2. Excel名称解析切面 3. Excel返回值处理器 4.
 * Excel请求参数解析器
 *
 * @author lengleng
 * @since 1.0.0
 */
@Import(ExcelHandlerConfiguration.class)
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ExcelConfigProperties.class)
public class ResponseExcelAutoConfiguration {

	/**
	 * Spring MVC请求映射处理器适配器
	 */
	private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

	/**
	 * Excel返回值处理器
	 */
	private final ResponseExcelReturnValueHandler responseExcelReturnValueHandler;

	/**
	 * 注册SPEL表达式解析处理器 用于解析Excel文件名称中的动态表达式
	 * @return NameProcessor Excel名称解析器
	 */
	@Bean
	@ConditionalOnMissingBean
	public NameProcessor nameProcessor() {
		return new NameSpelExpressionProcessor();
	}

	/**
	 * 注册Excel名称解析处理切面 用于处理动态Excel文件名称的解析
	 * @param nameProcessor SPEL表达式解析处理器
	 * @return DynamicNameAspect 动态名称切面
	 */
	@Bean
	@ConditionalOnMissingBean
	public DynamicNameAspect dynamicNameAspect(NameProcessor nameProcessor) {
		return new DynamicNameAspect(nameProcessor);
	}

	/**
	 * 注册Excel返回值处理器到Spring MVC 将Excel返回值处理器添加到Spring MVC的返回值处理器列表中
	 */
	@PostConstruct
	public void setReturnValueHandlers() {
		List<HandlerMethodReturnValueHandler> returnValueHandlers = requestMappingHandlerAdapter
				.getReturnValueHandlers();

		List<HandlerMethodReturnValueHandler> newHandlers = new ArrayList<>();
		newHandlers.add(responseExcelReturnValueHandler);
		assert returnValueHandlers != null;
		newHandlers.addAll(returnValueHandlers);
		requestMappingHandlerAdapter.setReturnValueHandlers(newHandlers);
	}

	/**
	 * 注册Excel请求参数解析器到Spring MVC 将Excel请求参数解析器添加到Spring MVC的参数解析器列表中
	 */
	@PostConstruct
	public void setRequestExcelArgumentResolver() {
		List<HandlerMethodArgumentResolver> argumentResolvers = requestMappingHandlerAdapter.getArgumentResolvers();
		List<HandlerMethodArgumentResolver> resolverList = new ArrayList<>();
		resolverList.add(new RequestExcelArgumentResolver());
		resolverList.addAll(argumentResolvers);
		requestMappingHandlerAdapter.setArgumentResolvers(resolverList);
	}

}

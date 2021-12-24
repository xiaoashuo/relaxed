package com.relaxed.common.exception;

import com.relaxed.common.exception.aop.*;
import com.relaxed.common.exception.handler.DefaultGlobalExceptionHandler;
import com.relaxed.common.exception.handler.GlobalExceptionHandler;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

/**
 * @author Yakir
 * @Topic ExceptionConfiguration
 * @Description
 * @date 2021/12/21 14:32
 * @Version 1.0
 */
@Configuration(proxyBeanMethods = false)
public class ExceptionAopAutoConfiguration {

	/**
	 * 默认的异常处理器
	 * @return DefaultExceptionHandler
	 */
	@Bean
	@ConditionalOnMissingBean(GlobalExceptionHandler.class)
	public GlobalExceptionHandler defaultGlobalExceptionHandler() {
		return new DefaultGlobalExceptionHandler();
	}

	/**
	 * 默认切点注册器
	 * @author yakir
	 * @date 2021/12/21 14:33
	 * @return com.relaxed.common.exception.aop.PointCutRegister
	 */
	@Bean
	@ConditionalOnMissingBean
	public PointCutRegister pointCutRegister() {
		return new DefaultPointCutRegister();
	}

	/**
	 * 通知者参数包装注册器
	 * @author yakir
	 * @date 2021/12/21 14:33
	 * @param pointCutRegister
	 * @param globalExceptionHandler
	 * @return com.relaxed.common.exception.aop.ExceptionAdvisorRegister
	 */
	@Bean
	@ConditionalOnMissingBean
	public ExceptionAdvisorRegister exceptionAdvisorRegister(PointCutRegister pointCutRegister,
			GlobalExceptionHandler globalExceptionHandler) {
		ExceptionAnnotationInterceptor interceptor = new ExceptionAnnotationInterceptor(globalExceptionHandler);
		return new ExceptionAdvisorRegister(pointCutRegister.build(), interceptor);
	}

	/**
	 * 通知者
	 * @author yakir
	 * @date 2021/12/21 14:33
	 * @param exceptionAdvisorRegister
	 * @return com.relaxed.common.exception.aop.ExceptionAnnotationAdvisor
	 */
	@Role(value = BeanDefinition.ROLE_INFRASTRUCTURE)
	@Bean
	@ConditionalOnMissingBean
	public ExceptionAnnotationAdvisor exceptionAnnotationAdvisor(ExceptionAdvisorRegister exceptionAdvisorRegister) {
		ExceptionAnnotationAdvisor advisor = new ExceptionAnnotationAdvisor(exceptionAdvisorRegister);
		advisor.setOrder(exceptionAdvisorRegister.getOrder());
		return advisor;
	}

}

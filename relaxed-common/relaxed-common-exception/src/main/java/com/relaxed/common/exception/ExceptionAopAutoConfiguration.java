package com.relaxed.common.exception;

import com.relaxed.common.exception.aop.*;
import com.relaxed.common.exception.handler.GlobalExceptionHandler;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.lang.Nullable;

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
	 * 默认切点注册器
	 * @author yakir
	 * @date 2021/12/21 14:33
	 * @return com.relaxed.common.exception.aop.PointCutRegister
	 */
	@Bean
	@ConditionalOnMissingBean
	public PointCutBuilder pointCutBuilder() {
		return new DefaultPointCutBuilder();
	}

	/**
	 * 通知者参数包装注册器
	 * @author yakir
	 * @date 2021/12/21 14:33
	 * @param pointCutBuilder
	 * @return com.relaxed.common.exception.aop.ExceptionAdvisorRegister
	 */
	@Bean
	@ConditionalOnMissingBean
	public ExceptionAdvisorRegister exceptionAdvisorRegister(PointCutBuilder pointCutBuilder,
			ExceptionAnnotationInterceptor exceptionAnnotationInterceptor) {
		return new ExceptionAdvisorRegister(pointCutBuilder.build(), exceptionAnnotationInterceptor);
	}

	/**
	 * 异常策略
	 * @author yakir
	 * @date 2021/12/24 18:10
	 * @return com.relaxed.common.exception.aop.ExceptionStrategy
	 */
	@Bean
	@ConditionalOnMissingBean
	public ExceptionStrategy exceptionStrategy() {
		return new ExceptionStrategy() {
			@Override
			public boolean nestedMulNotice() {
				return false;
			}
		};
	}

	/**
	 * 异常注解拦截器
	 * @author yakir
	 * @date 2021/12/24 18:10
	 * @param exceptionStrategy
	 * @param globalExceptionHandler
	 * @return com.relaxed.common.exception.aop.ExceptionAnnotationInterceptor
	 */
	@Bean
	@ConditionalOnMissingBean
	public ExceptionAnnotationInterceptor exceptionAnnotationInterceptor(ExceptionStrategy exceptionStrategy,
			GlobalExceptionHandler globalExceptionHandler) {
		return new ExceptionAnnotationInterceptor(exceptionStrategy, globalExceptionHandler);
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

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
 * 异常处理AOP自动配置类
 * <p>
 * 提供基于AOP的异常处理自动配置，包括切点构建、通知注册、异常拦截等功能。 通过注解方式实现异常的统一处理和通知。
 * </p>
 *
 * @author Yakir
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
public class ExceptionAopAutoConfiguration {

	/**
	 * 创建默认的切点构建器
	 * <p>
	 * 用于构建异常处理的切点，确定需要拦截的方法。 当没有自定义的切点构建器时，使用默认实现。
	 * </p>
	 * @return 默认的切点构建器实例
	 */
	@Bean
	@ConditionalOnMissingBean
	public PointCutBuilder pointCutBuilder() {
		return new DefaultPointCutBuilder();
	}

	/**
	 * 创建异常通知注册器
	 * <p>
	 * 用于注册异常通知相关的配置，包括切点和异常拦截器。 当没有自定义的注册器时，使用默认实现。
	 * </p>
	 * @param pointCutBuilder 切点构建器
	 * @param exceptionAnnotationInterceptor 异常注解拦截器
	 * @return 异常通知注册器实例
	 */
	@Bean
	@ConditionalOnMissingBean
	public ExceptionAdvisorRegister exceptionAdvisorRegister(PointCutBuilder pointCutBuilder,
			ExceptionAnnotationInterceptor exceptionAnnotationInterceptor) {
		return new ExceptionAdvisorRegister(pointCutBuilder.build(), exceptionAnnotationInterceptor);
	}

	/**
	 * 创建异常注解拦截器
	 * <p>
	 * 用于拦截和处理带有异常注解的方法。 当没有自定义的拦截器时，使用默认实现。
	 * </p>
	 * @param globalExceptionHandler 全局异常处理器
	 * @return 异常注解拦截器实例
	 */
	@Bean
	@ConditionalOnMissingBean
	public ExceptionAnnotationInterceptor exceptionAnnotationInterceptor(
			GlobalExceptionHandler globalExceptionHandler) {
		return new ExceptionAnnotationInterceptor(globalExceptionHandler);
	}

	/**
	 * 创建异常注解通知者
	 * <p>
	 * 用于将异常通知注册器与AOP框架集成。 当没有自定义的通知者时，使用默认实现。
	 * </p>
	 * @param exceptionAdvisorRegister 异常通知注册器
	 * @return 异常注解通知者实例
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

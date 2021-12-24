package com.relaxed.autoconfigure.web.exception;

import cn.hutool.core.bean.BeanUtil;
import com.relaxed.common.exception.ExceptionHandleConfig;
import com.relaxed.common.exception.annotation.ExceptionNotice;
import com.relaxed.common.exception.aop.*;
import com.relaxed.common.exception.handler.DefaultGlobalExceptionHandler;
import com.relaxed.common.exception.handler.DingTalkGlobalExceptionHandler;
import com.relaxed.common.exception.handler.GlobalExceptionHandler;
import com.relaxed.common.exception.handler.MailGlobalExceptionHandler;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yakir
 * @Topic ExceptionAutoConfiguration
 * @Description
 * @date 2021/12/21 10:12
 * @Version 1.0
 */
@Configuration(proxyBeanMethods = false)
public class ExceptionConfiguration {

	/**
	 * 异常通知切点
	 * @author yakir
	 * @date 2021/12/22 11:24
	 * @return com.relaxed.common.exception.aop.PointCutRegister
	 */
	@Bean
	@ConditionalOnMissingBean
	public PointCutRegister exceptionNotice() {
		return () -> {
			Pointcut cpc = new AnnotationMatchingPointcut(ExceptionNotice.class, true);
			Pointcut mpc = new AnnotationMethodPoint(ExceptionNotice.class);
			Pointcut restMpc = new AnnotationMatchingPointcut(RestController.class);
			return new ComposablePointcut(cpc).union(mpc).union(restMpc);
		};
	}

}

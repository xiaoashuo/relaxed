package com.relaxed.autoconfigure.web.exception;

import com.relaxed.common.exception.annotation.ExceptionNotice;
import com.relaxed.common.exception.aop.*;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

/**
 * 异常配置类 配置异常通知相关的切点和切面 支持通过@ExceptionNotice注解标记需要通知的方法
 *
 * @author Yakir
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
public class ExceptionConfiguration {

	/**
	 * 创建异常通知切点 支持类级别和方法级别的@ExceptionNotice注解
	 * @return 异常通知切点构建器
	 */
	@Bean
	@ConditionalOnMissingBean
	public PointCutBuilder exceptionNotice() {
		return () -> {
			Pointcut cpc = new AnnotationMatchingPointcut(ExceptionNotice.class, true);
			Pointcut mpc = new AnnotationMethodPoint(ExceptionNotice.class);
			return new ComposablePointcut(cpc).union(mpc);
		};
	}

}

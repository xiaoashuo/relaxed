package com.relaxed.common.exception.aop;

import com.relaxed.common.exception.annotation.ExceptionNotice;
import org.aopalliance.aop.Advice;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.StaticMethodMatcher;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Yakir
 * @Topic DefaultExceptionRegister
 * @Description
 * @date 2021/12/21 10:15
 * @Version 1.0
 */
public class ExceptionAdvisorRegister {

	private final Pointcut pointcut;

	private final Advice advice;

	/**
	 * 切面顺序
	 */
	private Integer order;

	public ExceptionAdvisorRegister(Pointcut pointcut, Advice advice) {
		this.pointcut = pointcut;
		this.advice = advice;
		this.order = Ordered.LOWEST_PRECEDENCE;
	}

	public Advice advice() {
		return this.advice;
	}

	public Pointcut pointCut() {
		return pointcut;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

}

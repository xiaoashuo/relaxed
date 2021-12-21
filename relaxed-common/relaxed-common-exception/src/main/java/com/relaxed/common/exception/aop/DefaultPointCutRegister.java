package com.relaxed.common.exception.aop;

import com.relaxed.common.exception.annotation.ExceptionNotice;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.StaticMethodMatcher;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Yakir
 * @Topic DefaultPointCutRegister
 * @Description
 * @date 2021/12/21 10:33
 * @Version 1.0
 */
public class DefaultPointCutRegister implements PointCutRegister {

	@Override
	public Pointcut build() {
		Pointcut cpc = new AnnotationMatchingPointcut(ExceptionNotice.class, true);
		Pointcut mpc = new AnnotationMethodPoint(ExceptionNotice.class);
		return new ComposablePointcut(cpc).union(mpc);
	}

}

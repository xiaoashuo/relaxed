package com.relaxed.common.log.operation.aspect;

import lombok.NonNull;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.StaticMethodMatcher;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Yakir
 * @Topic LogOperatorAdvisor
 * @Description
 * @date 2023/12/14 14:46
 * @Version 1.0
 */
public class LogOperatorAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

	private final Advice advice;

	private final Pointcut pointcut;

	private final Class<? extends Annotation> annotation;

	public LogOperatorAdvisor(@NonNull MethodInterceptor advice, @NonNull Class<? extends Annotation> annotation) {
		if (advice == null) {
			throw new NullPointerException("advice is marked non-null but is null");
		}
		else if (annotation == null) {
			throw new NullPointerException("annotation is marked non-null but is null");
		}
		else {
			this.advice = advice;
			this.annotation = annotation;
			this.pointcut = this.buildPointcut();
		}
	}

	@Override
	public Pointcut getPointcut() {
		return this.pointcut;
	}

	@Override
	public Advice getAdvice() {
		return this.advice;
	}

	private Pointcut buildPointcut() {
		Pointcut cpc = new AnnotationMatchingPointcut(this.annotation, true);
		Pointcut mpc = new LogOperatorPointCut(this.annotation);
		return (new ComposablePointcut(cpc)).union(mpc);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		if (this.advice instanceof BeanFactoryAware) {
			((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
		}
	}

	private static class LogOperatorPointCut implements Pointcut {

		private final Class<? extends Annotation> annotationType;

		public LogOperatorPointCut(Class<? extends Annotation> annotationType) {
			Assert.notNull(annotationType, "Annotation type must not be null");
			this.annotationType = annotationType;
		}

		@Override
		public ClassFilter getClassFilter() {
			return ClassFilter.TRUE;
		}

		@Override

		public MethodMatcher getMethodMatcher() {
			return new AnnotationMethodMatcher(this.annotationType);
		}

		private static class AnnotationMethodMatcher extends StaticMethodMatcher {

			private final Class<? extends Annotation> annotationType;

			public AnnotationMethodMatcher(Class<? extends Annotation> annotationType) {
				this.annotationType = annotationType;
			}

			@Override

			public boolean matches(Method method, Class<?> targetClass) {
				if (this.matchesMethod(method)) {
					return true;
				}
				else if (Proxy.isProxyClass(targetClass)) {
					return false;
				}
				else {
					Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
					return specificMethod != method && this.matchesMethod(specificMethod);
				}
			}

			private boolean matchesMethod(Method method) {
				return AnnotatedElementUtils.hasAnnotation(method, this.annotationType);
			}

		}

	}

}

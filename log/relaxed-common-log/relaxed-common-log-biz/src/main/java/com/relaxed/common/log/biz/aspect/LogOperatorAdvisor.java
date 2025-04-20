package com.relaxed.common.log.biz.aspect;

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
 * 日志操作切面顾问类，负责管理日志操作的切面配置。 该类继承自 AbstractPointcutAdvisor，实现了 BeanFactoryAware 接口，
 * 用于创建基于注解的切点，并管理日志操作的拦截器。 主要功能： 1. 配置切点以拦截指定注解标记的方法 2. 管理日志操作的拦截器 3. 支持类级别和方法级别的注解检测
 *
 * @author Yakir
 * @since 1.0.0
 */
public class LogOperatorAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

	/**
	 * 日志操作的拦截器
	 */
	private final Advice advice;

	/**
	 * 用于匹配需要拦截的方法的切点
	 */
	private final Pointcut pointcut;

	/**
	 * 要处理的注解类型
	 */
	private final Class<? extends Annotation> annotation;

	/**
	 * 构造函数，初始化日志操作顾问。
	 * @param advice 日志操作的拦截器，不能为 null
	 * @param annotation 要处理的注解类型，不能为 null
	 * @throws NullPointerException 如果 advice 或 annotation 为 null
	 */
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

	/**
	 * 构建用于匹配注解的切点。 将类级别和方法级别的切点组合成一个复合切点。
	 * @return 复合切点
	 */
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

	/**
	 * 日志操作切点实现类，用于匹配带有指定注解的方法。 支持继承层次中的注解检测。
	 */
	private static class LogOperatorPointCut implements Pointcut {

		/**
		 * 要匹配的注解类型
		 */
		private final Class<? extends Annotation> annotationType;

		/**
		 * 构造函数
		 * @param annotationType 要匹配的注解类型，不能为 null
		 */
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

		/**
		 * 注解方法匹配器，用于检查方法是否带有指定注解。 支持检查方法本身的注解和其在父类中的原始方法的注解。
		 */
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

			/**
			 * 检查方法是否带有指定的注解
			 * @param method 要检查的方法
			 * @return 如果方法带有指定注解则返回 true
			 */
			private boolean matchesMethod(Method method) {
				return AnnotatedElementUtils.hasAnnotation(method, this.annotationType);
			}

		}

	}

}

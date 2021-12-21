package com.relaxed.common.exception.aop;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * @author Yakir
 * @Topic ExceptionAnnotationAdvisor通知
 * @Description
 * @date 2021/12/21 10:05
 * @Version 1.0
 */
public class ExceptionAnnotationAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

	private final Advice advice;

	private final Pointcut pointcut;

	public ExceptionAnnotationAdvisor(ExceptionAdvisorRegister exceptionAdvisorRegister) {
		this.advice = exceptionAdvisorRegister.advice();
		this.pointcut = exceptionAdvisorRegister.pointCut();
	}

	@Override
	public Pointcut getPointcut() {
		return this.pointcut;
	}

	@Override
	public Advice getAdvice() {
		return this.advice;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		if (this.advice instanceof BeanFactoryAware) {
			((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
		}
	}

}

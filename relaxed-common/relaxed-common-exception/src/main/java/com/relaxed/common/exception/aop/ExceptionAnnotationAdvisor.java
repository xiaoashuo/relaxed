package com.relaxed.common.exception.aop;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * 异常注解通知者
 * <p>
 * 继承自 {@link AbstractPointcutAdvisor}，实现了 {@link BeanFactoryAware} 接口。 用于将异常通知注册器与Spring
 * AOP框架集成，支持BeanFactory感知。
 * </p>
 *
 * @author Yakir
 * @since 1.0.0
 */
public class ExceptionAnnotationAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

	/**
	 * 通知对象
	 * <p>
	 * 用于定义异常处理的具体逻辑。
	 * </p>
	 */
	private final Advice advice;

	/**
	 * 切点对象
	 * <p>
	 * 用于确定需要被异常处理拦截的方法范围。
	 * </p>
	 */
	private final Pointcut pointcut;

	/**
	 * 构造函数
	 * <p>
	 * 基于异常通知注册器创建通知者实例。
	 * </p>
	 * @param exceptionAdvisorRegister 异常通知注册器
	 */
	public ExceptionAnnotationAdvisor(ExceptionAdvisorRegister exceptionAdvisorRegister) {
		this.advice = exceptionAdvisorRegister.advice();
		this.pointcut = exceptionAdvisorRegister.pointCut();
	}

	/**
	 * 获取切点对象
	 * @return 切点对象
	 */
	@Override
	public Pointcut getPointcut() {
		return this.pointcut;
	}

	/**
	 * 获取通知对象
	 * @return 通知对象
	 */
	@Override
	public Advice getAdvice() {
		return this.advice;
	}

	/**
	 * 设置BeanFactory
	 * <p>
	 * 如果通知对象实现了 {@link BeanFactoryAware} 接口，则设置其BeanFactory。
	 * </p>
	 * @param beanFactory Spring BeanFactory
	 * @throws BeansException 如果设置BeanFactory时发生异常
	 */
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		if (this.advice instanceof BeanFactoryAware) {
			((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
		}
	}

}

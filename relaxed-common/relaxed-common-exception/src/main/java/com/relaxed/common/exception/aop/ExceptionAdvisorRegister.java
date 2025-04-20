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
 * 异常通知注册器
 * <p>
 * 用于注册和管理异常处理的切点和通知。 支持自定义切面顺序，默认使用最低优先级。
 * </p>
 *
 * @author Yakir
 * @since 1.0.0
 */
public class ExceptionAdvisorRegister {

	/**
	 * 切点对象
	 * <p>
	 * 用于确定需要被异常处理拦截的方法范围。
	 * </p>
	 */
	private final Pointcut pointcut;

	/**
	 * 通知对象
	 * <p>
	 * 用于定义异常处理的具体逻辑。
	 * </p>
	 */
	private final Advice advice;

	/**
	 * 切面顺序
	 * <p>
	 * 控制多个切面的执行顺序，数值越小优先级越高。 默认使用最低优先级。
	 * </p>
	 */
	private Integer order;

	/**
	 * 构造函数
	 * <p>
	 * 创建异常通知注册器实例，初始化切点和通知。
	 * </p>
	 * @param pointcut 切点对象
	 * @param advice 通知对象
	 */
	public ExceptionAdvisorRegister(Pointcut pointcut, Advice advice) {
		this.pointcut = pointcut;
		this.advice = advice;
		this.order = Ordered.LOWEST_PRECEDENCE;
	}

	/**
	 * 获取通知对象
	 * @return 通知对象
	 */
	public Advice advice() {
		return this.advice;
	}

	/**
	 * 获取切点对象
	 * @return 切点对象
	 */
	public Pointcut pointCut() {
		return pointcut;
	}

	/**
	 * 获取切面顺序
	 * @return 切面顺序值
	 */
	public Integer getOrder() {
		return order;
	}

	/**
	 * 设置切面顺序
	 * @param order 切面顺序值
	 */
	public void setOrder(Integer order) {
		this.order = order;
	}

}

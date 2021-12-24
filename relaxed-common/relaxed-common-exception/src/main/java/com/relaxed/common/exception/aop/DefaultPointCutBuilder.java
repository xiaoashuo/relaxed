package com.relaxed.common.exception.aop;

import com.relaxed.common.exception.annotation.ExceptionNotice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

/**
 * @author Yakir
 * @Topic DefaultPointCutRegister
 * @Description
 * @date 2021/12/21 10:33
 * @Version 1.0
 */
public class DefaultPointCutBuilder implements PointCutBuilder {

	@Override
	public Pointcut build() {
		Pointcut cpc = new AnnotationMatchingPointcut(ExceptionNotice.class, true);
		Pointcut mpc = new AnnotationMethodPoint(ExceptionNotice.class);
		return new ComposablePointcut(cpc).union(mpc);
	}

}

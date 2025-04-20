package com.relaxed.common.exception.aop;

import com.relaxed.common.exception.annotation.ExceptionNotice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

/**
 * 默认切点构建器实现类
 * <p>
 * 实现了 {@link PointCutBuilder} 接口，提供了基于 {@link ExceptionNotice} 注解的切点构建逻辑。
 * 支持类级别和方法级别的注解匹配。
 * </p>
 *
 * @author Yakir
 * @since 1.0.0
 */
public class DefaultPointCutBuilder implements PointCutBuilder {

	/**
	 * 构建切点
	 * <p>
	 * 创建基于 {@link ExceptionNotice} 注解的切点，包括： 1. 类级别的注解匹配 2. 方法级别的注解匹配 3.
	 * 将两种匹配方式组合成一个完整的切点
	 * </p>
	 * @return 组合后的切点对象
	 */
	@Override
	public Pointcut build() {
		Pointcut cpc = new AnnotationMatchingPointcut(ExceptionNotice.class, true);
		Pointcut mpc = new AnnotationMethodPoint(ExceptionNotice.class);
		return new ComposablePointcut(cpc).union(mpc);
	}

}

package com.relaxed.common.exception.aop;

import org.springframework.aop.Pointcut;

/**
 * 切点构建器接口
 * <p>
 * 定义了构建AOP切点的规范，用于确定需要被异常处理拦截的方法。 实现类需要提供具体的切点构建逻辑。
 * </p>
 *
 * @author Yakir
 * @since 1.0.0
 */
public interface PointCutBuilder {

	/**
	 * 构建切点
	 * <p>
	 * 根据实现类的逻辑构建AOP切点，确定需要被拦截的方法范围。
	 * </p>
	 * @return 构建好的切点对象
	 */
	Pointcut build();

}

package com.relaxed.common.exception.aop;

import org.springframework.aop.Pointcut;

/**
 * @author Yakir
 * @Topic PointCutRegiter
 * @Description
 * @date 2021/12/21 10:30
 * @Version 1.0
 */
public interface PointCutBuilder {

	/**
	 * 切点构建
	 * @author yakir
	 * @date 2021/12/21 10:30
	 * @return org.springframework.aop.Pointcut
	 */
	Pointcut build();

}

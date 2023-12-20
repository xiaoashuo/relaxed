package com.relaxed.common.log.operation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Yakir
 * @Topic OperatorLog
 * @Description
 * @date 2023/12/14 11:11
 * @Version 1.0
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface LogFunc {

	/**
	 * 命名空间
	 */
	String namespace() default "";

	/**
	 * 方法名
	 */
	String funcName() default "";

	/**
	 * 执行时机
	 * @return
	 */
	String around() default "";

}

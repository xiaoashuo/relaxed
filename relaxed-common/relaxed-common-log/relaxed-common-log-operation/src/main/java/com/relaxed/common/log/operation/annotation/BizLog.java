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
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface BizLog {

	/**
	 * 系统名称
	 * @return
	 */
	String systemName() default "";

	/**
	 * 操作日志成功得文本模板 require true
	 */
	String success();

	/**
	 * 操作日志执行人 require false
	 */
	String operator() default "";

	/**
	 * 操作类型：比如增删改查
	 */
	String type() default "";

	/**
	 * 操作日志失败的文本模板 require false
	 */
	String fail() default "";

	/**
	 * 操作日志绑定的业务标识对象 require true
	 */
	String bizNo();

	/**
	 * 扩展参数 记录操作日志的修改详情 require false
	 */
	String detail() default "";

	/**
	 * 记录日志的条件
	 * @return
	 */
	String condition() default "true";

	/**
	 * 是否记录返回值 true: 记录返回值 false: 不记录返回值
	 */
	boolean recordReturnValue() default true;

}

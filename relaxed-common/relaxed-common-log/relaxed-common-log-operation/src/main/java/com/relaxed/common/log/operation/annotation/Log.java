package com.relaxed.common.log.operation.annotation;

import java.lang.annotation.*;

/**
 * @author Yakir
 * @Topic Log
 * @Description
 * @date 2021/6/27 12:32
 * @Version 1.0
 */
@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

	/**
	 * 日志分组
	 * @return
	 */
	String group() default "default";

	/**
	 * 日志消息
	 * @return 日志消息描述
	 */
	String msg() default "";

	/**
	 * 日志操作类型
	 * @return 日志操作类型
	 */
	int type();

	/**
	 * 是否保存方法入参
	 * @return boolean
	 */
	boolean recordParams() default true;

	/**
	 * 是否保存方法返回值
	 * @return boolean
	 */
	boolean recordResult() default true;

}

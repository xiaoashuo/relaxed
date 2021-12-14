package com.relaxed.common.log.operation.annotation;

import com.relaxed.common.log.operation.enums.OperationTypeEnum;

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
	 * 消息分组
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
	 * @return 日志操作类型枚举
	 */
	OperationTypeEnum type();

}

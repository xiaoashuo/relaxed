package com.relaxed.common.log.biz.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志函数注解，用于标记可以在日志模板中调用的函数方法。 通过该注解可以将一个方法注册为日志模板中的函数，支持： - 在不同的命名空间下注册函数 - 自定义函数名称 -
 * 控制函数的执行时机 该注解可以应用在方法或类级别，并支持继承。
 *
 * @author Yakir
 * @since 1.0.0
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface LogFunc {

	/**
	 * 函数的命名空间，用于对函数进行分组管理。 可以避免不同模块之间的函数名冲突。 例如："user"、"order"、"system" 等。
	 * @return 命名空间名称，默认为空字符串
	 */
	String namespace() default "";

	/**
	 * 函数名称，用于在日志模板中引用该函数。 如果不指定，将使用方法的原始名称。 例如：将方法 "getUserName" 注册为 "userName" 函数
	 * @return 函数名称，默认为空字符串
	 */
	String funcName() default "";

	/**
	 * 函数的执行时机，用于控制函数在日志记录过程中的调用时间点。 支持 SpEL 表达式，可以根据上下文动态决定是否执行。 例如："#{#result !=
	 * null}"表示只在有返回值时执行
	 * @return 执行条件表达式，默认为空字符串
	 */
	String around() default "";

}

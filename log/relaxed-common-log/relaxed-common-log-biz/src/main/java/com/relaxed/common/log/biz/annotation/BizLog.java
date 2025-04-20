package com.relaxed.common.log.biz.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 业务操作日志注解，用于标记需要记录业务操作日志的方法。 支持记录操作的系统、模块、操作类型、操作人、业务编号等信息， 并可以通过模板定义成功和失败时的日志内容。
 * 该注解支持继承，可以被子类继承使用。
 *
 * @author Yakir
 * @since 1.0.0
 */
@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface BizLog {

	/**
	 * 系统名称，用于标识操作所属的系统。 当一个应用服务多个系统时，可以用来区分不同系统的日志。
	 * @return 系统名称，默认为空字符串
	 */
	String systemName() default "";

	/**
	 * 模块名称，用于标识操作所属的功能模块。 可以帮助更细粒度地分类和查询日志。
	 * @return 模块名称，默认为空字符串
	 */
	String moduleName() default "";

	/**
	 * 操作成功时的日志文本模板。 支持 SpEL 表达式，可以引用方法参数、返回值等信息。 例如："用户 #{#operator} 成功创建了订单 #{#bizNo}"
	 * @return 成功日志模板，默认为空字符串
	 */
	String success() default "";

	/**
	 * 操作失败时的日志文本模板。 支持 SpEL 表达式，可以引用方法参数、异常信息等。 例如："用户 #{#operator} 创建订单 #{#bizNo}
	 * 失败，原因：#{#error.message}"
	 * @return 失败日志模板，默认为空字符串
	 */
	String fail() default "";

	/**
	 * 操作执行人标识。 支持 SpEL 表达式，可以从上下文中获取当前操作人信息。
	 * @return 操作执行人标识，默认为空字符串
	 */
	String operator() default "";

	/**
	 * 操作类型，用于标识具体的操作行为。 建议使用统一的操作类型常量，如：CREATE、UPDATE、DELETE、QUERY等。
	 * @return 操作类型，默认为空字符串
	 */
	String type() default "";

	/**
	 * 业务标识，用于关联具体的业务对象。 支持 SpEL 表达式，通常使用业务对象的唯一标识。 例如："#{#order.orderId}" 或
	 * "#{#user.userId}"
	 * @return 业务标识，默认为空字符串
	 */
	String bizNo() default "";

	/**
	 * 操作详情，用于记录更详细的操作信息。 支持 SpEL 表达式，可以记录修改前后的数据对比等信息。
	 * @return 操作详情，默认为空字符串
	 */
	String detail() default "";

	/**
	 * 日志记录条件，用于控制是否记录日志。 支持 SpEL 表达式，可以根据运行时条件决定是否记录日志。 例如："#{#result != null}" 或
	 * "#{#user.type == 'ADMIN'}"
	 * @return 条件表达式，默认为 "true"，表示始终记录日志
	 */
	String condition() default "true";

	/**
	 * 是否记录方法返回值。 当设置为 true 时，会将方法的返回值记录到日志中。 注意：对于返回值较大的方法，建议设置为 false 以避免日志数据过大。
	 * @return true 表示记录返回值，false 表示不记录
	 */
	boolean recordReturnValue() default true;

}

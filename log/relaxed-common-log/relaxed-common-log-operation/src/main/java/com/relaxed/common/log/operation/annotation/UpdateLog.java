package com.relaxed.common.log.operation.annotation;

import com.relaxed.common.log.operation.enums.OperationTypes;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 更新操作日志注解 用于标记执行更新操作的方法,自动记录操作日志 该注解继承自 {@link Log} 注解,并指定操作类型为更新
 *
 * @author Yakir
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Log(type = OperationTypes.UPDATE)
public @interface UpdateLog {

	/**
	 * 日志分组名称 用于对日志进行分类管理,默认为"default"
	 * @return 分组名称
	 */
	@AliasFor(annotation = Log.class)
	String group() default "default";

	/**
	 * 操作日志描述信息 用于描述当前更新操作的具体内容
	 * @return 日志描述信息
	 */
	@AliasFor(annotation = Log.class)
	String msg();

	/**
	 * 是否记录方法参数 控制是否将方法参数记录到日志中,默认为true
	 * @return 是否记录参数
	 */
	@AliasFor(annotation = Log.class)
	boolean recordParams() default true;

	/**
	 * 是否记录方法返回值 控制是否将方法返回值记录到日志中,默认为true
	 * @return 是否记录返回值
	 */
	@AliasFor(annotation = Log.class)
	boolean recordResult() default true;

}

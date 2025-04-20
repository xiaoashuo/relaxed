package com.relaxed.common.idempotent.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 幂等控制注解。 用于标记需要保证幂等性的方法，防止重复请求，主要功能包括： 1. 自定义幂等键前缀 2. 支持 SpEL 表达式提取唯一标识 3. 可配置幂等控制时长 4.
 * 支持自定义提示信息 5. 可配置幂等键清理策略
 *
 * @author Yakir
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {

	/**
	 * 幂等键前缀常量
	 */
	String KEY_PREFIX = "idem";

	/**
	 * 幂等标识的前缀，可用于区分服务和业务，防止 key 冲突。 完整的幂等标识格式为：{prefix}:{uniqueExpression.value}
	 * @return 业务标识前缀
	 */
	String prefix() default KEY_PREFIX;

	/**
	 * SpEL 表达式，用于从上下文中提取幂等的唯一性标识。
	 * @return Spring-EL 表达式
	 */
	String uniqueExpression() default "";

	/**
	 * 幂等的控制时长，必须大于业务的处理耗时。 其值为幂等 key 的标记时长，超过标记时间，则幂等 key 可再次使用。
	 * @return 标记时长，默认 10 分钟
	 */
	long duration() default 10 * 60;

	/**
	 * 控制时长单位。
	 * @return 时间单位，默认为秒
	 */
	TimeUnit timeUnit() default TimeUnit.SECONDS;

	/**
	 * 重复请求时的提示信息。
	 * @return 提示信息
	 */
	String message() default "重复请求，请稍后重试";

	/**
	 * 是否在业务完成后立刻清除幂等 key。
	 * @return true 表示立刻清除，false 表示不处理
	 */
	boolean removeKeyWhenFinished() default false;

	/**
	 * 是否在业务执行异常时立刻清除幂等 key。
	 * @return true 表示立刻清除，false 表示不处理
	 */
	boolean removeKeyWhenError() default false;

}

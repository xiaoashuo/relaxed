package com.relaxed.common.redis.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 缓存更新注解，用于方法级别的缓存更新。 利用AOP在方法执行后执行缓存put操作，将方法的返回值存入缓存中。 若方法返回null，则会默认存入一个nullValue。
 *
 * @author Yakir
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@MetaCacheAnnotation
public @interface CachePut {

	/**
	 * 缓存key的前缀
	 * @return 缓存key的前缀
	 */
	String prefix() default "";

	/**
	 * 如果需要在key后面拼接参数，则传入一个拼接数据的SpEL表达式
	 * @return SpEL表达式
	 */
	String keyJoint() default "";

	/**
	 * 条件筛选，符合条件才会更新缓存
	 * @return SpEL条件表达式
	 */
	String condition() default "";

	/**
	 * 超时时间 ttl = 0: 使用全局配置值 ttl &lt; 0: 不超时 ttl &gt; 0: 使用此超时时间
	 * @return 超时时间
	 */
	long ttl() default 0;

	/**
	 * 控制时长单位，默认为秒
	 * @return 时间单位
	 */
	TimeUnit timeUnit() default TimeUnit.SECONDS;

}

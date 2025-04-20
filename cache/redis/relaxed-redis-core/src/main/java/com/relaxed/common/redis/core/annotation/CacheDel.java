package com.relaxed.common.redis.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 缓存删除注解，用于方法级别的缓存删除。 利用AOP在方法执行后执行缓存删除操作。
 *
 * @author Yakir
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@MetaCacheAnnotation
public @interface CacheDel {

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
	 * 条件筛选，符合条件才会删除缓存
	 * @return SpEL条件表达式
	 */
	String condition() default "";

	/**
	 * 是否清除多个key 当值为true时，强制要求keyJoint有值，且SpEL表达式解析结果为Collection
	 * @return 是否清除多个key
	 */
	boolean multiDel() default false;

}

package com.relaxed.common.cache.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author Yakir
 * @Topic Cached
 * @Description 利用Aop, 在方法执行后执行缓存删除操作
 * @date 2021/7/23 16:22
 * @Version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@MetaCacheAnnotation
public @interface CacheDel {

	/**
	 * cache key 前缀
	 * @return
	 */
	String prefix() default "";

	/**
	 * key name support SpEL
	 * @return
	 */
	String key() default "";

	/**
	 * key 后缀
	 * @return
	 */
	String suffix() default "";

	/**
	 * key 生成bean 与 prefix 互斥 key 互斥
	 * @return
	 */
	String keyGenerator() default "";

	/**
	 * 条件筛选 符合条件才会缓存
	 * @return
	 */
	String condition() default "";

}

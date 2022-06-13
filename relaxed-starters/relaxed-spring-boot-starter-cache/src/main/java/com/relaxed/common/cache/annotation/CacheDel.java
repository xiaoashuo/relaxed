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
	 * 如果需要在key 后面拼接参数 则传入一个拼接数据的 SpEL 表达式
	 */
	String keyJoint() default "";

	/**
	 * 条件筛选 符合条件才会缓存
	 * @return
	 */
	String condition() default "";

}

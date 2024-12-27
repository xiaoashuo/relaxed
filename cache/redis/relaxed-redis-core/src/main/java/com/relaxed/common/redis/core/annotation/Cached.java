package com.relaxed.common.redis.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author Yakir
 * @Topic Cached
 * @Description 利用Aop, 在方法调用前先查询缓存 若缓存中没有数据，则调用方法本身，并将方法返回值放置入缓存中 分布式锁 获取 可以自行替换 缓存实现
 * @date 2021/7/23 16:22
 * @Version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@MetaCacheAnnotation
public @interface Cached {

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

	/**
	 * 超时时间(S) ttl = 0 使用全局配置值 ttl < 0 : 不超时 ttl > 0 : 使用此超时间
	 * @return long
	 */
	long ttl() default 0;

	/**
	 * 控制时长单位，默认为 SECONDS 秒
	 * @return {@link TimeUnit}
	 */
	TimeUnit timeUnit() default TimeUnit.SECONDS;

}

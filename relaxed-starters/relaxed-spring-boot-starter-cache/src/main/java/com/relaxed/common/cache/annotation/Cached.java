package com.relaxed.common.cache.annotation;

import java.lang.annotation.*;

/**
 * @author Yakir
 * @Topic Cached
 * @Description 利用Aop, 在方法调用前先查询缓存 若缓存中没有数据，则调用方法本身，并将方法返回值放置入缓存中
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
     * key name support SpEL
     * @return
     */
    String key() default "";

    /**
     * 超时时间(S) ttl = 0 使用全局配置值 ttl < 0 : 不超时 ttl > 0 : 使用此超时间
     * @return long
     */
    long ttl() default 0;
}

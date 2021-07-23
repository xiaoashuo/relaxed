package com.relaxed.common.cache.annotation;

import java.lang.annotation.*;

/**
 * @author Yakir
 * @Topic Cached
 * @Description 利用Aop, 在方法执行后执行缓存put操作 将方法的返回值置入缓存中，若方法返回null，则会默认置入一个nullValue
 * @date 2021/7/23 16:22
 * @Version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@MetaCacheAnnotation
public @interface CachedPut {

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

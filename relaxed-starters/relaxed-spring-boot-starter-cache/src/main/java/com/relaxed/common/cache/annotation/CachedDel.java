package com.relaxed.common.cache.annotation;

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
public @interface CachedDel {

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


}

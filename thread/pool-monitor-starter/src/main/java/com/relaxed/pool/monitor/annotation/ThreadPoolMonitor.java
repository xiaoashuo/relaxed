package com.relaxed.pool.monitor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Yakir
 * @Topic ThreadPoolMonitor
 * @Description
 * @date 2025/4/4 10:10
 * @Version 1.0
 */
@Retention(RetentionPolicy.RUNTIME) // 必须添加这个保留策略
@Target(ElementType.METHOD)
public @interface ThreadPoolMonitor {

	/**
	 * 线程池监控key 默认取bean名称 若有值 则取值
	 */
	String name() default "";

}

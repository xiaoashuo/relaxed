package com.relaxed.pool.monitor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 线程池监控注解，用于标记需要被监控的线程池方法。 该注解在运行时生效，只能应用于方法级别。
 *
 * @author Yakir
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ThreadPoolMonitor {

	/**
	 * 线程池监控的标识名称。 如果未指定，则默认使用被注解方法所在类的 bean 名称作为监控标识。 如果指定了值，则使用指定的值作为监控标识。
	 * @return 线程池监控标识名称
	 */
	String name() default "";

}

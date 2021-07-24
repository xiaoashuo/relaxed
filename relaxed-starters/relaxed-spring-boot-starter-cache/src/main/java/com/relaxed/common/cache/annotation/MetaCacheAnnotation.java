package com.relaxed.common.cache.annotation;

import java.lang.annotation.*;

/**
 * @author Yakir
 * @Topic MetaCacheAnnotation
 * @Description 缓存元注释
 * @date 2021/7/23 16:23
 * @Version 1.0
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MetaCacheAnnotation {

	/**
	 * Spring Expression Language (SpEL) expression used for making the cache put
	 * operation conditional.
	 * <p>
	 * This expression is evaluated after the method has been called due to the nature of
	 * the put operation and can therefore refer to the {@code result}.
	 * <p>
	 * Default is {@code ""}, meaning the method result is always cached.
	 * <p>
	 * The SpEL expression evaluates against a dedicated context that provides the
	 * following meta-data:
	 * <ul>
	 * <li>{@code #result} for a reference to the result of the method invocation. For
	 * supported wrappers such as {@code Optional}, {@code #result} refers to the actual
	 * object, not the wrapper</li>
	 * <li>{@code #root.method}, {@code #root.target}, and {@code #root.caches} for
	 * references to the {@link java.lang.reflect.Method method}, target object, and
	 * affected cache(s) respectively.</li>
	 * <li>Shortcuts for the method name ({@code #root.methodName}) and target class
	 * ({@code #root.targetClass}) are also available.
	 * <li>Method arguments can be accessed by index. For instance the second argument can
	 * be accessed via {@code #root.args[1]}, {@code #p1} or {@code #a1}. Arguments can
	 * also be accessed by name if that information is available.</li>
	 * </ul>
	 */
	String condition() default "";

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
	 * key 生成bean 与 prefix key suffix 互斥
	 * @return
	 */
	String keyGenerator() default "";

}

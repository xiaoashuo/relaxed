package com.relaxed.common.redis.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
	 * 如果需要在key 后面拼接参数 则传入一个拼接数据的 SpEL 表达式
	 */
	String keyJoint() default "";

}

package com.relaxed.common.datascope.annotation;

import java.lang.annotation.*;

/**
 * 数据权限注解
 * <p>
 * 用于标注 Mapper 类或方法，指定需要进行数据权限控制的实体信息。 可以通过该注解配置数据权限的包含和排除规则。
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPermission {

	/**
	 * 是否忽略数据权限控制
	 * <p>
	 * 设置为 true 时，当前类或方法将跳过数据权限控制。
	 * @return 是否忽略数据权限，默认为 false
	 */
	boolean ignore() default false;

	/**
	 * 指定需要进行数据权限控制的资源类型
	 * <p>
	 * 当数组不为空时，只对指定的资源类型进行数据权限控制。 如果设置了该属性，则 excludeResources 属性将失效。
	 * @return 资源类型数组
	 */
	String[] includeResources() default {};

	/**
	 * 指定需要跳过数据权限控制的资源类型
	 * <p>
	 * 当数组不为空时，对指定的资源类型跳过数据权限控制。 如果 includeResources 属性不为空，则该属性将失效。
	 * @return 资源类型数组
	 */
	String[] excludeResources() default {};

}

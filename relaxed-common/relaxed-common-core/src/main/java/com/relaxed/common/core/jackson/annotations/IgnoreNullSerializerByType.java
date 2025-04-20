package com.relaxed.common.core.jackson.annotations;

import java.lang.annotation.*;

/**
 * 忽略空值序列化注解，用于指定类型级别的空值序列化处理策略 主要用于解决 {@code @JsonInclude} 注解在某些情况下不生效的问题
 *
 * @author Yakir
 * @since 1.0.0
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface IgnoreNullSerializerByType {

	IgnoreNullSerializerByType.Include value() default IgnoreNullSerializerByType.Include.ALL;

	public static enum Include {

		ALL, STRING, ARRAY, MAP,;

		Include() {
		}

	}

}

package com.relaxed.common.core.jackson.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 忽略空值序列化注解，用于指定字段级别的空值序列化处理策略 主要用于解决 {@code @JsonInclude} 注解在某些情况下不生效的问题
 *
 * @author Yakir
 * @since 1.0.0
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface IgnoreNullSerializer {

}

package com.relaxed.common.exception.annotation;

import java.lang.annotation.*;

/**
 * 异常通知注解
 * <p>
 * 用于标记需要异常通知的类或方法。 被标记的类或方法在执行过程中发生异常时，会触发异常通知机制。
 * </p>
 *
 * @author Yakir
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
public @interface ExceptionNotice {

}

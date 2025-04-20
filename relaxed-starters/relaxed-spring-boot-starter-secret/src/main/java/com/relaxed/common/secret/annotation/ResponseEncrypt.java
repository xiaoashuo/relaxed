package com.relaxed.common.secret.annotation;

import java.lang.annotation.*;

/**
 * 响应加密注解 用于标记需要加密的响应处理方法或类 被标记的方法或类返回的响应体将被加密处理
 *
 * @author Yakir
 * @since 1.0
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ResponseEncrypt {

}

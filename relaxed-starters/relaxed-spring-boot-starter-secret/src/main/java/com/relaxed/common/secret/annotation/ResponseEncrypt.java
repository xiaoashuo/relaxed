package com.relaxed.common.secret.annotation;

import java.lang.annotation.*;

/**
 * @author Yakir
 * @Topic ResponseEncrypt
 * @Description
 * @date 2021/11/15 10:08
 * @Version 1.0
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ResponseEncrypt {

}

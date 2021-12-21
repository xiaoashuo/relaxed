package com.relaxed.common.exception.annotation;

import java.lang.annotation.*;

/**
 * @author Yakir
 * @Topic ExceptionNotice
 * @Description
 * @date 2021/12/21 9:59
 * @Version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
public @interface ExceptionNotice {

}

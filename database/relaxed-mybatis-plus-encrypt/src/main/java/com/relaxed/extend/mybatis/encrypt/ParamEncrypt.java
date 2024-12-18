package com.relaxed.extend.mybatis.encrypt;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Yakir
 * @Topic ParamEncrypt
 * @Description 标识入参是否需要加密,仅作用在字符串类型的参数上
 * @date 2024/10/13 11:02
 * @Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface ParamEncrypt {

}

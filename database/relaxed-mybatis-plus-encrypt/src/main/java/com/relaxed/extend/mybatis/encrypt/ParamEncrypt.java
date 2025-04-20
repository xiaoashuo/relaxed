package com.relaxed.extend.mybatis.encrypt;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 参数加密注解 用于标记需要加密的方法参数，仅支持字符串类型的参数 被标记的参数在SQL执行前会自动进行加密处理
 * @author Yakir
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface ParamEncrypt {

}

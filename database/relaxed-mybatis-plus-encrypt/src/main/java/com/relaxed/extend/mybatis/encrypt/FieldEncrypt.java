package com.relaxed.extend.mybatis.encrypt;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段加密注解 用于标记需要加密的字段或类，支持字段级别和类级别的加密 被标记的字段在数据库操作时会自动进行加密和解密处理
 * @author Yakir
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.TYPE })
public @interface FieldEncrypt {

}

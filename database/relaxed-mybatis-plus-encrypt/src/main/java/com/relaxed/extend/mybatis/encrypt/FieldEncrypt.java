package com.relaxed.extend.mybatis.encrypt;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Yakir
 * @Topic FieldEncrypt
 * @Description 标识字段是否需要加解密,加载类和字段上
 * @date 2024/10/9 16:22
 * @Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.TYPE })
public @interface FieldEncrypt {

}

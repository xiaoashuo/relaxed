package com.relaxed.common.core.jackson.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Yakir
 * @Topic IgnoreNullSerializer
 * @Description 忽略空序列化处理器 主要为解决引入@JsonInclude(value =
 * JsonInclude.Include.NON_EMPTY||JsonInclude.Include.NON_NULL) 部分不生效问题 因为 会被自定义空序列化器处理
 * @See BeanPropertyWriter#serializeAsField 方法 693行
 * @date 2021/8/4 20:44
 * @Version 1.0
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface IgnoreNullSerializer {

}

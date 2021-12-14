package com.relaxed.common.log.action.annotation;

import com.relaxed.common.log.action.converter.DefaultTypeDiffConverter;
import com.relaxed.common.log.action.converter.DiffConverter;
import com.relaxed.common.log.action.converter.NullTypeConverter;

import java.lang.annotation.*;

/**
 * @author Yakir
 * @Topic LogTag
 * @Description
 * @date 2021/12/14 13:46
 * @Version 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogTag {

	/**
	 * 字段别名
	 * @return
	 */
	String alias() default "";

	/**
	 * 转换器
	 * @return
	 */
	Class<? extends DiffConverter> converter() default NullTypeConverter.class;

}

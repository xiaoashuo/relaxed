package com.relaxed.common.log.biz.annotation;

import com.relaxed.common.log.biz.extractor.DiffExtractor;
import com.relaxed.common.log.biz.extractor.SimpleTypeDiffExtractor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
public @interface LogDiffTag {

	/**
	 * 字段别名
	 * @return
	 */
	String alias() default "";

	/**
	 * 当前处理字段类型 别名
	 * @return
	 */
	String typeAlias() default "";

	/**
	 * 是否忽略当前字段
	 * @return
	 */
	boolean ignore() default false;

	/**
	 * 差异提取器
	 * @return
	 */
	Class<? extends DiffExtractor> extractor() default SimpleTypeDiffExtractor.class;

}

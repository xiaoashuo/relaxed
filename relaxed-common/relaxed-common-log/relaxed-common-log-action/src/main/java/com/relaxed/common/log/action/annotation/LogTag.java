package com.relaxed.common.log.action.annotation;

import com.relaxed.common.log.action.extractor.DiffExtractor;
import com.relaxed.common.log.action.extractor.SimpleTypeDiffExtractor;

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
	 * 当前处理字段类型 别名
	 * @return
	 */
	String typeAlias() default "";

	/**
	 * 差异提取器
	 * @return
	 */
	Class<? extends DiffExtractor> extractor() default SimpleTypeDiffExtractor.class;

}

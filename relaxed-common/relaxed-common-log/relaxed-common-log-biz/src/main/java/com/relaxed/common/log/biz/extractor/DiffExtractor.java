package com.relaxed.common.log.biz.extractor;

import com.relaxed.common.log.biz.annotation.LogDiffTag;

import java.lang.reflect.Field;

/**
 * @author Yakir
 * @Topic Converter
 * @Description
 * @date 2021/12/14 17:26
 * @Version 1.0
 */
public interface DiffExtractor {

	/**
	 * 转换出 差异值
	 * @author yakir
	 * @date 2021/12/14 17:29
	 * @param field
	 * @param logDiffTag
	 * @param oldFieldValue
	 * @param newFieldValue
	 * @return String
	 */
	String diffValue(Field field, LogDiffTag logDiffTag, Object oldFieldValue, Object newFieldValue);

}

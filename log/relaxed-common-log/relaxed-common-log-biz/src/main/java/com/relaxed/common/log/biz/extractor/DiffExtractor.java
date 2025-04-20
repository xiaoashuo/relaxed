package com.relaxed.common.log.biz.extractor;

import com.relaxed.common.log.biz.annotation.LogDiffTag;

import java.lang.reflect.Field;

/**
 * 差异值提取器接口，用于提取和转换对象字段的差异值。 该接口定义了如何将对象字段的旧值和新值转换为可读的差异描述。 主要用于日志记录中展示对象属性的变化情况。
 *
 * @author Yakir
 * @since 1.0.0
 */
public interface DiffExtractor {

	/**
	 * 提取并转换字段的差异值
	 * @param field 字段对象，包含字段的元数据信息
	 * @param logDiffTag 差异标签注解，包含字段的差异处理配置
	 * @param oldFieldValue 字段的旧值
	 * @param newFieldValue 字段的新值
	 * @return 转换后的差异值字符串描述
	 */
	String diffValue(Field field, LogDiffTag logDiffTag, Object oldFieldValue, Object newFieldValue);

}

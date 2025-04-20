package com.relaxed.common.log.biz.service;

import com.relaxed.common.log.biz.annotation.LogDiffTag;
import com.relaxed.common.log.biz.model.AttributeModel;

import java.lang.reflect.Field;

/**
 * 字段处理器接口，用于处理对象属性的差异比较 该接口定义了属性比较的基本规范，实现类需要提供具体的比较逻辑 支持自定义属性的比较规则和忽略规则
 *
 * @author Yakir
 */
public interface IFieldHandler {

	/**
	 * 提取属性模型 根据字段的差异标签和值变化，生成属性变更模型
	 * @param field 当前处理的字段
	 * @param logDiffTag 字段的差异标签，包含字段的配置信息
	 * @param oldFieldValue 字段的旧值
	 * @param newFieldValue 字段的新值
	 * @return 属性变更模型，包含字段的变化信息
	 */
	AttributeModel extractAttributeModel(Field field, LogDiffTag logDiffTag, Object oldFieldValue,
			Object newFieldValue);

	/**
	 * 判断是否忽略当前字段 根据字段的类型、值和配置决定是否忽略该字段的差异比较
	 * @param clazzType 字段所属的类类型
	 * @param field 当前处理的字段
	 * @param oldFieldValue 字段的旧值
	 * @param newFieldValue 字段的新值
	 * @return true 表示忽略该字段，false 表示不忽略
	 */
	boolean ignoreField(Class clazzType, Field field, Object oldFieldValue, Object newFieldValue);

}
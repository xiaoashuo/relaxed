package com.relaxed.common.log.biz.service;

import com.relaxed.common.log.biz.annotation.LogTag;
import com.relaxed.common.log.biz.model.AttributeModel;

import java.lang.reflect.Field;

/**
 * @author Yakir
 * @Topic FieldHandler
 * @Description
 * @date 2021/12/14 14:50
 * @Version 1.0
 */
public interface IFieldHandler {

	/**
	 * 提取属性model
	 * @author yakir
	 * @date 2021/12/14 16:29
	 * @param logTag
	 * @param oldFieldValue
	 * @param newFieldValue
	 * @return com.relaxed.common.log.action.model.AttributeModel
	 */
	AttributeModel extractAttributeModel(Field field, LogTag logTag, Object oldFieldValue, Object newFieldValue);

	/**
	 * 是否忽略当前处理字段 忽略 则不进行属性变更记录
	 * @author yakir
	 * @date 2021/12/16 9:35
	 * @param clazzType 类元素类型
	 * @param field
	 * @param oldFieldValue
	 * @param newFieldValue
	 * @return boolean true 忽略 false 不忽略
	 */
	boolean ignoreField(Class clazzType, Field field, Object oldFieldValue, Object newFieldValue);

}

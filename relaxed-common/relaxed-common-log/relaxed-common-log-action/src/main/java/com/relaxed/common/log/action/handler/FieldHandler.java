package com.relaxed.common.log.action.handler;

import com.relaxed.common.log.action.annotation.LogTag;
import com.relaxed.common.log.action.model.AttributeModel;

import java.lang.reflect.Field;

/**
 * @author Yakir
 * @Topic FieldHandler
 * @Description
 * @date 2021/12/14 14:50
 * @Version 1.0
 */
public interface FieldHandler {

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
	 * 是否忽略当前处理字段
	 * @author yakir
	 * @date 2021/12/16 9:35
	 * @param field
	 * @return boolean true 忽略 false 不忽略
	 */
	boolean ignoreField(Field field);

}

package com.relaxed.common.log.action.handler.impl;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.relaxed.common.log.action.annotation.LogTag;
import com.relaxed.common.log.action.converter.DiffConvertHolder;
import com.relaxed.common.log.action.converter.DiffConverter;
import com.relaxed.common.log.action.handler.FieldHandler;
import com.relaxed.common.log.action.model.AttributeModel;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * @author Yakir
 * @Topic DefaultFieldHandle
 * @Description
 * @date 2021/12/14 14:50
 * @Version 1.0
 */
public class DefaultFieldHandler implements FieldHandler {

	@Override
	public AttributeModel extractAttributeModel(Object oldFieldValue, Object newFieldValue) {
		AttributeModel attributeModel = new AttributeModel();
		return null;
	}

	@Override
	public AttributeModel extractAttributeModel(Field field, LogTag logTag, Object oldFieldValue,
			Object newFieldValue) {
		AttributeModel attributeModel = new AttributeModel();
		Class<?> fieldType = field.getType();
		String fieldName = field.getName();
		attributeModel.setAttributeType(fieldType.getSimpleName());
		attributeModel.setAttributeName(fieldName);
		String alias = logTag == null ? fieldName : logTag.alias();
		attributeModel.setAttributeAlias(alias);
		// 效验字段类型
		boolean basicHandleType = ClassUtil.isBasicType(fieldType) || LocalDateTime.class.isAssignableFrom(fieldType);
		// 基本类型不比较差异值
		if (basicHandleType) {
			String oldValueStr = StrUtil.toString(oldFieldValue);
			String newValueStr = StrUtil.toString(newFieldValue);
			attributeModel.setOldValue(oldValueStr);
			attributeModel.setNewValue(newValueStr);
			return attributeModel;
		}
		// 非基本类型
		String oldValueStr = JSONUtil.toJsonStr(oldFieldValue);
		String newValueStr = JSONUtil.toJsonStr(newFieldValue);
		attributeModel.setOldValue(oldValueStr);
		attributeModel.setNewValue(newValueStr);
		DiffConverter diffConverter = logTag == null ? DiffConvertHolder.getByDefault()
				: DiffConvertHolder.getByClass(logTag.converter());
		String diffValue = diffConverter.diffValue(field, logTag, oldFieldValue, newFieldValue);
		attributeModel.setDiffValue(diffValue);
		return attributeModel;
	}

}

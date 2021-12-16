package com.relaxed.common.log.action.handler.impl;

import cn.hutool.json.JSONUtil;
import com.relaxed.common.log.action.annotation.LogTag;
import com.relaxed.common.log.action.converter.DiffConvertHolder;
import com.relaxed.common.log.action.converter.DiffExtractor;
import com.relaxed.common.log.action.handler.FieldHandler;
import com.relaxed.common.log.action.model.AttributeModel;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

/**
 * @author Yakir
 * @Topic DefaultFieldHandle
 * @Description
 * @date 2021/12/14 14:50
 * @Version 1.0
 */
public class DefaultFieldHandler implements FieldHandler {

	@Override
	public AttributeModel extractAttributeModel(Field field, LogTag logTag, Object oldFieldValue,
			Object newFieldValue) {
		AttributeModel attributeModel = new AttributeModel();
		Class<?> fieldType = field.getType();
		String fieldTypeSimpleName = fieldType.getSimpleName();
		String fieldName = field.getName();
		attributeModel.setAttributeName(fieldName);
		String oldValueStr = JSONUtil.toJsonStr(oldFieldValue);
		String newValueStr = JSONUtil.toJsonStr(newFieldValue);
		attributeModel.setOldValue(oldValueStr);
		attributeModel.setNewValue(newValueStr);
		DiffExtractor diffExtractor;
		if (logTag == null) {
			attributeModel.setAttributeType(fieldTypeSimpleName);
			attributeModel.setAttributeAlias(fieldName);
			diffExtractor = DiffConvertHolder.getByDefault();

		}
		else {
			String typeAlias = logTag.typeAlias();
			String alias = logTag.alias();
			attributeModel.setAttributeType(StringUtils.hasText(typeAlias) ? typeAlias : fieldTypeSimpleName);
			attributeModel.setAttributeAlias(StringUtils.hasText(alias) ? alias : fieldName);
			diffExtractor = DiffConvertHolder.getByClass(logTag.extractor());
		}
		String diffValue = diffExtractor.diffValue(field, logTag, oldFieldValue, newFieldValue);
		attributeModel.setDiffValue(diffValue);
		return attributeModel;
	}

	@Override
	public boolean ignoreField(Field field) {
		return false;
	}

}

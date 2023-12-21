package com.relaxed.common.log.biz.service.impl;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import com.relaxed.common.log.biz.annotation.LogTag;
import com.relaxed.common.log.biz.extractor.DiffConvertHolder;
import com.relaxed.common.log.biz.extractor.DiffExtractor;
import com.relaxed.common.log.biz.model.AttributeModel;
import com.relaxed.common.log.biz.service.IFieldHandler;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

/**
 * @author Yakir
 * @Topic DefaultFieldHandle
 * @Description
 * @date 2021/12/14 14:50
 * @Version 1.0
 */
public class DefaultFieldHandler implements IFieldHandler {

	@Override
	public AttributeModel extractAttributeModel(Field field, LogTag logTag, Object oldFieldValue,
			Object newFieldValue) {
		AttributeModel attributeModel = new AttributeModel();
		Class<?> fieldType = field.getType();
		String fieldTypeSimpleName = fieldType.getSimpleName();
		String fieldName = field.getName();
		attributeModel.setAttributeName(fieldName);
		String oldValueStr;
		String newValueStr;
		if (ClassUtil.isBasicType(fieldType)) {
			oldValueStr = StrUtil.toString(oldFieldValue);
			newValueStr = StrUtil.toString(newFieldValue);
		}
		else {
			oldValueStr = JSONUtil.toJsonStr(oldFieldValue);
			newValueStr = JSONUtil.toJsonStr(newFieldValue);
		}

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
	public boolean ignoreField(Class clazzType, Field field, Object oldFieldValue, Object newFieldValue) {
		return false;
	}

}

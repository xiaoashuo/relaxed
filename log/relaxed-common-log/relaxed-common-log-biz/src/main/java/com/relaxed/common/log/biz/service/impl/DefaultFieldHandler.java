package com.relaxed.common.log.biz.service.impl;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import com.relaxed.common.log.biz.annotation.LogDiffTag;
import com.relaxed.common.log.biz.extractor.DiffConvertHolder;
import com.relaxed.common.log.biz.extractor.DiffExtractor;
import com.relaxed.common.log.biz.model.AttributeModel;
import com.relaxed.common.log.biz.service.IFieldHandler;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

/**
 * 默认字段处理器实现类 该实现类提供了对象字段差异比较的默认处理逻辑 主要功能包括： 1. 提取字段的属性信息，包括字段名、类型、别名等 2.
 * 处理字段值的序列化，支持基本类型和复杂对象 3. 使用差异提取器计算字段的差异值 4. 支持通过注解配置字段的别名和类型别名
 *
 * @author Yakir
 */
public class DefaultFieldHandler implements IFieldHandler {

	/**
	 * 提取字段属性模型 根据字段信息和注解配置，构建完整的属性模型 处理流程： 1. 设置字段名称 2. 根据字段类型处理值的序列化 3. 设置字段类型和别名 4.
	 * 使用差异提取器计算差异值
	 * @param field 字段对象
	 * @param logDiffTag 差异比较注解
	 * @param oldFieldValue 旧字段值
	 * @param newFieldValue 新字段值
	 * @return 属性模型
	 */
	@Override
	public AttributeModel extractAttributeModel(Field field, LogDiffTag logDiffTag, Object oldFieldValue,
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
		if (logDiffTag == null) {
			attributeModel.setAttributeType(fieldTypeSimpleName);
			attributeModel.setAttributeAlias(fieldName);
			diffExtractor = DiffConvertHolder.getByDefault();

		}
		else {
			String typeAlias = logDiffTag.typeAlias();
			String alias = logDiffTag.alias();
			attributeModel.setAttributeType(StringUtils.hasText(typeAlias) ? typeAlias : fieldTypeSimpleName);
			attributeModel.setAttributeAlias(StringUtils.hasText(alias) ? alias : fieldName);
			diffExtractor = DiffConvertHolder.getByClass(logDiffTag.extractor());
		}
		String diffValue = diffExtractor.diffValue(field, logDiffTag, oldFieldValue, newFieldValue);
		attributeModel.setDiffValue(diffValue);
		return attributeModel;
	}

	/**
	 * 判断是否忽略字段 默认实现不忽略任何字段，业务方可以根据需要重写此方法 例如： 1. 忽略特定类型的字段 2. 忽略特定名称的字段 3. 根据字段值判断是否忽略
	 * @param clazzType 类类型
	 * @param field 字段对象
	 * @param oldFieldValue 旧字段值
	 * @param newFieldValue 新字段值
	 * @return 是否忽略该字段
	 */
	@Override
	public boolean ignoreField(Class clazzType, Field field, Object oldFieldValue, Object newFieldValue) {
		return false;
	}

}
package com.relaxed.common.log.biz.service.impl;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;

import com.relaxed.common.log.biz.annotation.LogDiffTag;
import com.relaxed.common.log.biz.model.AttributeModel;
import com.relaxed.common.log.biz.model.DiffMeta;
import com.relaxed.common.log.biz.service.IDataHandler;
import com.relaxed.common.log.biz.service.IFieldHandler;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yakir
 * @Topic DefaultDataHandler
 * @Description 数据处理器 提取数据
 * @date 2021/12/14 14:13
 * @Version 1.0
 */
@RequiredArgsConstructor
public class DefaultDataHandler implements IDataHandler {

	private final IFieldHandler fieldHandler;

	@Override
	public List<AttributeModel> diffObject(DiffMeta diffMeta) {
		Object oldValue = diffMeta.getSource();
		Object newValue = diffMeta.getTarget();
		Class<?> oldValueClass = oldValue.getClass();
		Class<?> newValueClass = newValue.getClass();
		ArrayList<AttributeModel> attributeModelList = new ArrayList<>();
		// 1.效验新旧元素是否为同一类型 ,同类型 比对差异 不同则不进行查询比对
		if (oldValueClass.equals(newValueClass)) {
			Field[] declaredFields = ClassUtil.getDeclaredFields(oldValueClass);
			for (Field declaredField : declaredFields) {

				LogDiffTag logDiffTag = AnnotationUtil.getAnnotation(declaredField, LogDiffTag.class);
				if (logDiffTag == null || logDiffTag.ignore()) {
					continue;
				}

				Object oldFieldValue = ReflectUtil.getFieldValue(oldValue, declaredField);
				Object newFieldValue = ReflectUtil.getFieldValue(newValue, declaredField);
				if (fieldHandler.ignoreField(oldValueClass, declaredField, oldFieldValue, newFieldValue)) {
					continue;
				}
				AttributeModel attributeModel = fieldHandler.extractAttributeModel(declaredField, logDiffTag,
						oldFieldValue, newFieldValue);
				attributeModelList.add(attributeModel);
			}
		}
		return attributeModelList;
	}

}

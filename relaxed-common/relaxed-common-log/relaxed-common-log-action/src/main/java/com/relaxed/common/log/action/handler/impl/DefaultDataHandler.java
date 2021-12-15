package com.relaxed.common.log.action.handler.impl;

import java.time.LocalDateTime;

import java.util.ArrayList;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.relaxed.common.log.action.annotation.LogTag;
import com.relaxed.common.log.action.handler.DataHandler;
import com.relaxed.common.log.action.handler.FieldHandler;
import com.relaxed.common.log.action.handler.RecordHandler;
import com.relaxed.common.log.action.model.AttributeModel;
import com.relaxed.common.log.action.model.OperationModel;
import com.relaxed.common.log.action.model.ReportModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @author Yakir
 * @Topic DefaultDataHandler
 * @Description 数据处理器 提取数据
 * @date 2021/12/14 14:13
 * @Version 1.0
 */
@RequiredArgsConstructor
public class DefaultDataHandler implements DataHandler {

	private final RecordHandler recordHandler;

	private final FieldHandler fieldHandler;

	@Override
	public void recordObject(OperationModel operationModel) {
		Object oldValue = operationModel.getOldValue();
		Object newValue = operationModel.getNewValue();
		Class<?> oldValueClass = oldValue.getClass();
		Class<?> newValueClass = newValue.getClass();
		// 构建报告
		ReportModel reportModel = ReportModel.of(operationModel);
		ArrayList<AttributeModel> attributeModelList = new ArrayList<>();
		// 1.效验新旧元素是否为同一类型
		if (oldValueClass.equals(newValueClass)) {
			Field[] declaredFields = ClassUtil.getDeclaredFields(oldValueClass);
			for (Field declaredField : declaredFields) {
				LogTag logTag = AnnotationUtil.getAnnotation(declaredField, LogTag.class);
				Object oldFieldValue = ReflectUtil.getFieldValue(oldValue, declaredField);
				Object newFieldValue = ReflectUtil.getFieldValue(newValue, declaredField);
				AttributeModel attributeModel = fieldHandler.extractAttributeModel(declaredField, logTag, oldFieldValue,
						newFieldValue);
				attributeModelList.add(attributeModel);
			}

		}
		else {
			// 类型不一致 直接进行 上报

		}
		reportModel.setAttributeModelList(attributeModelList);
		recordHandler.report(reportModel);
	}

}

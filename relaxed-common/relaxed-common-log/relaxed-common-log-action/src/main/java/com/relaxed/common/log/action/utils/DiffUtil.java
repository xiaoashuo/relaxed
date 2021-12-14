package com.relaxed.common.log.action.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditorSupport;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONUtil;
import com.relaxed.common.log.action.model.AttributeModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.relaxed.common.log.action.model.ReportModel;
import difflib.DiffUtils;

/**
 * @author Yakir
 * @Topic DiffUtil
 * @Description 差异工具
 * @date 2021/12/14 13:58
 * @Version 1.0
 */
public class DiffUtil {

	private static class Ts {

		/**
		 * 所属对象 eg：object
		 */
		private String source;

		/**
		 * 字段名称
		 */
		private String fieldName;

		/**
		 * 层级
		 */
		private Integer level;

	}

	/**
	 * { username:"12", "classInfo":{ classes:1 } }
	 * @param args
	 */
	public static void main(String[] args) throws IntrospectionException {

		Map<String, String> diffMap = new HashMap<>();
		List<AttributeModel> list = new ArrayList<>();
		ReportModel reportModel = buildModel();
		ReportModel reportModel1 = buildModel();
		for (Field declaredField : reportModel.getClass().getDeclaredFields()) {
			Object oldFieldValue = ReflectUtil.getFieldValue(reportModel, declaredField);
			Object newFieldValue = ReflectUtil.getFieldValue(reportModel1, declaredField);
			String fieldName = declaredField.getName();
			Class<?> fieldType = declaredField.getType();
			boolean basicHandleType = ClassUtil.isBasicType(fieldType) || String.class.isAssignableFrom(fieldType)
					|| LocalDateTime.class.isAssignableFrom(fieldType);
			System.out.println(basicHandleType);
			if (basicHandleType) {
				// 若为基础数据类型 直接使用equals //基础字段不采用差异值
				if (!ObjectUtil.equals(oldFieldValue, newFieldValue)) {
					diffMap.put(fieldName, "原始值:" + oldFieldValue + "---新值:" + newFieldValue);
					AttributeModel attributeModel = new AttributeModel();
					attributeModel.setAttributeType(fieldType.getSimpleName());
					attributeModel.setAttributeName(fieldName);
					attributeModel.setAttributeAlias(fieldName);
					attributeModel.setOldValue(JSONUtil.toJsonStr(oldFieldValue));
					attributeModel.setNewValue(JSONUtil.toJsonStr(newFieldValue));
					attributeModel.setDiffValue("");
					list.add(attributeModel);
				}
			}
			else {
				Class<?> innerType = declaredField.getType();

				PropertyDescriptor pd = new PropertyDescriptor(fieldName, innerType);
				System.out.println("t");
			}

		}
		System.out.println(diffMap);
		System.out.println(list);

	}

	private static ReportModel buildModel() {
		ReportModel reportModel = new ReportModel();

		reportModel.setAppName("测试应用" + IdUtil.objectId());
		reportModel.setObjectName("ReportModel.class");
		reportModel.setObjectId(IdUtil.simpleUUID());
		reportModel.setOperator("System");
		reportModel.setOperationName("update");
		reportModel.setOperationAlias("更新");
		reportModel.setExtraWords("create_user");
		reportModel.setComment("测试");
		reportModel.setOperationTime(LocalDateTime.now());
		reportModel.setOldValue("");
		reportModel.setNewValue("");
		reportModel.setAttributeModelList(new ArrayList<AttributeModel>());
		return reportModel;
	}

}

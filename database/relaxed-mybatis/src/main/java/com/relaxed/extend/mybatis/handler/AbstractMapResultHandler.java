package com.relaxed.extend.mybatis.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;

import com.relaxed.extend.mybatis.annotation.MapResult;
import com.relaxed.extend.mybatis.decorator.ResultSetDecorator;
import com.relaxed.extend.mybatis.exception.MapResultException;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yakir
 * @Topic AbstractMapResultHandler
 * @Description
 * @date 2021/10/29 16:36
 * @Version 1.0
 */
public abstract class AbstractMapResultHandler implements MapResultHandler {

	@Override
	public Map resultToMap(ResultSetDecorator decorator, Method method, MapResult mapResultAnnotation)
			throws SQLException {
		// 结果集
		ResultSet resultSet = decorator.getResultSet();
		// 参数化类型 key val
		Pair<Class, Class> kvTypePair = getKVTypeOfReturnMap(method);
		String resultKey = mapResultAnnotation.key();
		boolean allowKeyRepeat = mapResultAnnotation.isAllowKeyRepeat();
		// 结果集 转储到map内
		Map<Object, Object> resultMap = new HashMap<>();
		// 返回类型
		Class returnType = kvTypePair.getValue();
		while (resultSet.next()) {
			Object key = decorator.getObject(kvTypePair.getKey(), resultKey);
			boolean basicType = ClassUtil.isBasicType(returnType);
			Object value;
			// 若为基本类型 则返回结果取值 只能为1
			if (basicType) {
				value = handleBaseType(decorator, mapResultAnnotation);
			}
			else if (String.class.isAssignableFrom(returnType)) {
				value = handleStringType(decorator, mapResultAnnotation);
			}
			else {
				value = handleObjectType(decorator, mapResultAnnotation, returnType);
			}
			putResultMap(resultMap, key, value, allowKeyRepeat);

		}
		return resultMap;
	}

	protected void putResultMap(Map<Object, Object> resultMap, Object key, Object value, Boolean allowKeyRepeat) {
		// 该key已存在
		if (resultMap.containsKey(key)) {
			// 判断是否允许key重复
			if (!allowKeyRepeat) {
				throw new MapResultException("MapResult duplicated  key exists ,value=" + key);
			}
		}
		resultMap.put(key, value);
	}

	protected Object handleObjectType(ResultSetDecorator decorator, MapResult mapResult, Class returnType)
			throws SQLException {
		String[] resultValueNames = mapResult.valueNames();
		int rstValueNameCount = resultValueNames.length;
		Map<String, Object> valueMap = new HashMap<>();
		if (rstValueNameCount == 0) {
			Map<String, Class> columnTypeMap = decorator.getColumnTypeMap();
			for (Map.Entry<String, Class> entry : columnTypeMap.entrySet()) {
				String columnName = entry.getKey();
				Class columnClass = entry.getValue();
				Object value = decorator.getObject(columnClass, columnName);
				valueMap.put(StrUtil.toCamelCase(columnName.toLowerCase()), value);
			}

		}
		else {
			for (String resultValueName : resultValueNames) {
				Class<?> columnType = decorator.getColumnType(resultValueName);
				Object value = decorator.getObject(columnType, resultValueName);
				valueMap.put(StrUtil.toCamelCase(resultValueName.toLowerCase()), value);
			}
		}
		return BeanUtil.mapToBean(valueMap, returnType, false, CopyOptions.create());
	}

	protected Object handleStringType(ResultSetDecorator decorator, MapResult mapResult) throws SQLException {
		String[] resultValueNames = mapResult.valueNames();
		int rstValueNameCount = resultValueNames.length;
		String valJoint = mapResult.valJoint();
		List<Object> values = new ArrayList<>();
		int columnCount;
		int nullCount = 0;
		if (rstValueNameCount == 0) {
			Map<String, Class> columnTypeMap = decorator.getColumnTypeMap();
			columnCount = decorator.getColumnCount();
			for (Map.Entry<String, Class> entry : columnTypeMap.entrySet()) {
				String columnName = entry.getKey();
				Class columnClass = entry.getValue();
				Object value = decorator.getObject(columnClass, columnName);
				if (value == null) {
					nullCount++;
				}
				values.add(value);
			}
		}
		else {
			columnCount = rstValueNameCount;
			for (String resultValueName : resultValueNames) {
				Class<?> columnType = decorator.getColumnType(resultValueName);
				Object value = decorator.getObject(columnType, resultValueName);
				if (value == null) {
					nullCount++;
				}
				values.add(value);
			}
		}

		// 若返回的值全为空 则直接放入null
		String value = null;
		if (nullCount != columnCount) {
			value = StrUtil.join(valJoint, values);
		}
		// 第一列作为key,第二列作为value。
		return value;
	}

	protected Object handleBaseType(ResultSetDecorator decorator, MapResult mapResultAnnotation) throws SQLException {
		String[] resultValueNames = mapResultAnnotation.valueNames();
		if (resultValueNames.length != 1) {
			throw new MapResultException("Annotation MapResult valueNames  cannot exceed 1 column.");
		}
		String resultValueName = resultValueNames[0];
		return decorator.getObject(resultValueName);

	}

	protected Pair<Class, Class> getKVTypeOfReturnMap(Method method) {
		Type methodReturnType = method.getGenericReturnType();
		if (methodReturnType instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) methodReturnType;
			if (!Map.class.isAssignableFrom((Class<?>) parameterizedType.getRawType())) {
				throw new MapResultException("return type must be map.");
			}
			Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
			return new Pair<>((Class<?>) actualTypeArguments[0], (Class<?>) actualTypeArguments[1]);

		}
		return new Pair<>(Object.class, Object.class);
	}

}

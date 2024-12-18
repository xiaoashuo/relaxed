package com.relaxed.extend.mybatis.decorator;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yakir
 * @Topic ResultSetDecorator
 * @Description 参考 org.apache.ibatis.executor.resultset.ResultSetWrapper
 * @date 2021/10/22 16:37
 * @Version 1.0
 */
public class ResultSetDecorator {

	private final TypeHandlerRegistry typeHandlerRegistry;

	private final Map<String, Class> columnTypeMap = new HashMap<>();

	private final List<String> columnNames = new ArrayList<>();

	private final Map<String, Map<Class<?>, TypeHandler<?>>> typeHandlerMap = new HashMap<>();

	private final List<JdbcType> jdbcTypes = new ArrayList<>();

	private final List<String> classNames = new ArrayList<>();

	private final ResultSet resultSet;

	private final Integer columnCount;

	public ResultSetDecorator(ResultSet rs, Configuration configuration) throws SQLException {
		super();
		this.typeHandlerRegistry = configuration.getTypeHandlerRegistry();
		this.resultSet = rs;
		final ResultSetMetaData metaData = rs.getMetaData();
		columnCount = metaData.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {
			String columnName = configuration.isUseColumnLabel() ? metaData.getColumnLabel(i)
					: metaData.getColumnName(i);
			columnNames.add(columnName);
			int columnType = metaData.getColumnType(i);
			JdbcType jdbcType = JdbcType.forCode(columnType);
			jdbcTypes.add(jdbcType);
			String columnClassName = metaData.getColumnClassName(i);
			classNames.add(columnClassName);
			columnTypeMap.put(columnName, resolveClass(columnClassName));
		}
	}

	public Object getObject(String columnName) throws SQLException {

		Class propertyClazz = getColumnType(columnName);
		return getObject(propertyClazz, columnName);
	}

	public Object getObject(Class<?> propType, String columnName) throws SQLException {
		columnName = columnName.toUpperCase();
		final TypeHandler<?> typeHandler;
		if (propType == null) {
			typeHandler = typeHandlerRegistry.getUnknownTypeHandler();
		}
		else {
			typeHandler = getTypeHandler(propType, columnName);
		}
		return typeHandler.getResult(resultSet, columnName);
	}

	public Map<String, Class> getColumnTypeMap() {
		return columnTypeMap;
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	public Integer getColumnCount() {
		return columnCount;
	}

	/**
	 * Gets the type handler to use when reading the result set. Tries to get from the
	 * TypeHandlerRegistry by searching for the property type. If not found it gets the
	 * column JDBC type and tries to get a handler for it.
	 * @param propertyType the property type
	 * @param columnName the column name
	 * @return the type handler
	 */
	public TypeHandler<?> getTypeHandler(Class<?> propertyType, String columnName) {
		TypeHandler<?> handler = null;
		Map<Class<?>, TypeHandler<?>> columnHandlers = typeHandlerMap.get(columnName);
		if (columnHandlers == null) {
			columnHandlers = new HashMap<>();
			typeHandlerMap.put(columnName, columnHandlers);
		}
		else {
			handler = columnHandlers.get(propertyType);
		}
		if (handler == null) {
			JdbcType jdbcType = getJdbcType(columnName);
			handler = typeHandlerRegistry.getTypeHandler(propertyType, jdbcType);
			// Replicate logic of UnknownTypeHandler#resolveTypeHandler
			// See issue #59 comment 10
			if (handler == null || handler instanceof UnknownTypeHandler) {
				final int index = columnNames.indexOf(columnName);
				final Class<?> javaType = resolveClass(classNames.get(index));
				if (javaType != null && jdbcType != null) {
					handler = typeHandlerRegistry.getTypeHandler(javaType, jdbcType);
				}
				else if (javaType != null) {
					handler = typeHandlerRegistry.getTypeHandler(javaType);
				}
				else if (jdbcType != null) {
					handler = typeHandlerRegistry.getTypeHandler(jdbcType);
				}
			}
			if (handler == null || handler instanceof UnknownTypeHandler) {
				handler = new ObjectTypeHandler();
			}
			columnHandlers.put(propertyType, handler);
		}
		return handler;
	}

	public JdbcType getJdbcType(String columnName) {
		for (int i = 0; i < columnNames.size(); i++) {
			if (columnNames.get(i).equalsIgnoreCase(columnName)) {
				return jdbcTypes.get(i);
			}
		}
		return null;
	}

	private Class<?> resolveClass(String className) {
		try {
			// #699 className could be null
			if (className != null) {
				return Resources.classForName(className);
			}
		}
		catch (ClassNotFoundException e) {
			// ignore
		}
		return null;
	}

	public Class<?> getColumnType(String columnName) {
		return columnTypeMap.get(columnName.toUpperCase());
	}

	public TypeHandlerRegistry getTypeHandlerRegistry() {
		return typeHandlerRegistry;
	}

}

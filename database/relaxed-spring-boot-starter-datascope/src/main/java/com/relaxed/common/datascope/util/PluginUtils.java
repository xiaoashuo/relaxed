/*
 * Copyright (c) 2011-2020, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.relaxed.common.datascope.util;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * MyBatis 插件工具类
 * <p>
 * 提供 MyBatis 插件开发所需的工具方法，包括代理对象处理、SQL 语句处理等。
 */
public abstract class PluginUtils {

	private PluginUtils() {
	}

	public static final String DELEGATE_BOUNDSQL_SQL = "delegate.boundSql.sql";

	/**
	 * 获取真实的目标对象
	 * <p>
	 * 处理多层代理的情况，返回最终的真实对象。
	 * @param target 可能被代理的对象
	 * @param <T> 目标对象类型
	 * @return 真实的目标对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T realTarget(Object target) {
		if (Proxy.isProxyClass(target.getClass())) {
			MetaObject metaObject = SystemMetaObject.forObject(target);
			return realTarget(metaObject.getValue("h.target"));
		}
		return (T) target;
	}

	/**
	 * 设置 BoundSql 的额外参数
	 * <p>
	 * 将额外的参数设置到 BoundSql 对象中。
	 * @param boundSql BoundSql 对象
	 * @param additionalParameters 额外参数映射
	 */
	public static void setAdditionalParameter(BoundSql boundSql, Map<String, Object> additionalParameters) {
		additionalParameters.forEach(boundSql::setAdditionalParameter);
	}

	/**
	 * 创建 MPBoundSql 对象
	 * <p>
	 * 封装 BoundSql 对象，提供更方便的访问方法。
	 * @param boundSql BoundSql 对象
	 * @return MPBoundSql 对象
	 */
	public static MPBoundSql mpBoundSql(BoundSql boundSql) {
		return new MPBoundSql(boundSql);
	}

	/**
	 * 创建 MPStatementHandler 对象
	 * <p>
	 * 封装 StatementHandler 对象，提供更方便的访问方法。
	 * @param statementHandler StatementHandler 对象
	 * @return MPStatementHandler 对象
	 */
	public static MPStatementHandler mpStatementHandler(StatementHandler statementHandler) {
		statementHandler = realTarget(statementHandler);
		MetaObject object = SystemMetaObject.forObject(statementHandler);
		return new MPStatementHandler(SystemMetaObject.forObject(object.getValue("delegate")));
	}

	/**
	 * MyBatis StatementHandler 封装类
	 * <p>
	 * 提供对 StatementHandler 内部属性的便捷访问。
	 */
	public static class MPStatementHandler {

		private final MetaObject statementHandler;

		MPStatementHandler(MetaObject statementHandler) {
			this.statementHandler = statementHandler;
		}

		/**
		 * 获取参数处理器
		 * @return ParameterHandler 对象
		 */
		public ParameterHandler parameterHandler() {
			return get("parameterHandler");
		}

		/**
		 * 获取 MappedStatement
		 * @return MappedStatement 对象
		 */
		public MappedStatement mappedStatement() {
			return get("mappedStatement");
		}

		/**
		 * 获取执行器
		 * @return Executor 对象
		 */
		public Executor executor() {
			return get("executor");
		}

		/**
		 * 获取封装的 BoundSql 对象
		 * @return MPBoundSql 对象
		 */
		public MPBoundSql mPBoundSql() {
			return new MPBoundSql(boundSql());
		}

		/**
		 * 获取 BoundSql 对象
		 * @return BoundSql 对象
		 */
		public BoundSql boundSql() {
			return get("boundSql");
		}

		/**
		 * 获取配置对象
		 * @return Configuration 对象
		 */
		public Configuration configuration() {
			return get("configuration");
		}

		@SuppressWarnings("unchecked")
		private <T> T get(String property) {
			return (T) statementHandler.getValue(property);
		}

	}

	/**
	 * BoundSql 封装类
	 * <p>
	 * 提供对 BoundSql 内部属性的便捷访问和修改。
	 */
	public static class MPBoundSql {

		private final MetaObject boundSql;

		private final BoundSql delegate;

		MPBoundSql(BoundSql boundSql) {
			this.delegate = boundSql;
			this.boundSql = SystemMetaObject.forObject(boundSql);
		}

		/**
		 * 获取 SQL 语句
		 * @return SQL 语句
		 */
		public String sql() {
			return delegate.getSql();
		}

		/**
		 * 设置 SQL 语句
		 * @param sql SQL 语句
		 */
		public void sql(String sql) {
			boundSql.setValue("sql", sql);
		}

		/**
		 * 获取参数映射列表
		 * @return 参数映射列表
		 */
		public List<ParameterMapping> parameterMappings() {
			List<ParameterMapping> parameterMappings = delegate.getParameterMappings();
			return new ArrayList<>(parameterMappings);
		}

		/**
		 * 设置参数映射列表
		 * @param parameterMappings 参数映射列表
		 */
		public void parameterMappings(List<ParameterMapping> parameterMappings) {
			boundSql.setValue("parameterMappings", Collections.unmodifiableList(parameterMappings));
		}

		/**
		 * 获取参数对象
		 * @return 参数对象
		 */
		public Object parameterObject() {
			return get("parameterObject");
		}

		/**
		 * 获取额外参数映射
		 * @return 额外参数映射
		 */
		public Map<String, Object> additionalParameters() {
			return get("additionalParameters");
		}

		@SuppressWarnings("unchecked")
		private <T> T get(String property) {
			return (T) boundSql.getValue(property);
		}

	}

}

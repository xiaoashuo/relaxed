package com.relaxed.common.tenant.utils;

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
 * MyBatis插件工具类 提供MyBatis插件开发所需的常用工具方法 包括代理对象处理、SQL参数处理等功能
 *
 * @author Yakir
 */
public class PluginUtils {

	/**
	 * BoundSql中SQL语句的属性路径
	 */
	public static final String DELEGATE_BOUNDSQL_SQL = "delegate.boundSql.sql";

	/**
	 * 获取真实的目标对象，处理多层代理的情况
	 * @param <T> 对象
	 * @param target 可能被代理的对象
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
	 * 设置BoundSql的额外参数
	 * @param boundSql BoundSql对象
	 * @param additionalParameters 额外参数映射
	 */
	public static void setAdditionalParameter(BoundSql boundSql, Map<String, Object> additionalParameters) {
		additionalParameters.forEach(boundSql::setAdditionalParameter);
	}

	/**
	 * 创建MPBoundSql对象
	 * @param boundSql BoundSql对象
	 * @return MPBoundSql对象
	 */
	public static MPBoundSql mpBoundSql(BoundSql boundSql) {
		return new MPBoundSql(boundSql);
	}

	/**
	 * 创建MPStatementHandler对象
	 * @param statementHandler StatementHandler对象
	 * @return MPStatementHandler对象
	 */
	public static MPStatementHandler mpStatementHandler(StatementHandler statementHandler) {
		statementHandler = realTarget(statementHandler);
		MetaObject object = SystemMetaObject.forObject(statementHandler);
		return new MPStatementHandler(SystemMetaObject.forObject(object.getValue("delegate")));
	}

	/**
	 * StatementHandler包装类 提供对StatementHandler的便捷访问方法
	 */
	public static class MPStatementHandler {

		private final MetaObject statementHandler;

		MPStatementHandler(MetaObject statementHandler) {
			this.statementHandler = statementHandler;
		}

		public ParameterHandler parameterHandler() {
			return get("parameterHandler");
		}

		public MappedStatement mappedStatement() {
			return get("mappedStatement");
		}

		public Executor executor() {
			return get("executor");
		}

		public MPBoundSql mPBoundSql() {
			return new MPBoundSql(boundSql());
		}

		public BoundSql boundSql() {
			return get("boundSql");
		}

		public Configuration configuration() {
			return get("configuration");
		}

		@SuppressWarnings("unchecked")
		private <T> T get(String property) {
			return (T) statementHandler.getValue(property);
		}

	}

	/**
	 * BoundSql包装类 提供对BoundSql的便捷访问和修改方法
	 */
	public static class MPBoundSql {

		private final MetaObject boundSql;

		private final BoundSql delegate;

		MPBoundSql(BoundSql boundSql) {
			this.delegate = boundSql;
			this.boundSql = SystemMetaObject.forObject(boundSql);
		}

		public String sql() {
			return delegate.getSql();
		}

		public void sql(String sql) {
			boundSql.setValue("sql", sql);
		}

		public List<ParameterMapping> parameterMappings() {
			List<ParameterMapping> parameterMappings = delegate.getParameterMappings();
			return new ArrayList<>(parameterMappings);
		}

		public void parameterMappings(List<ParameterMapping> parameterMappings) {
			boundSql.setValue("parameterMappings", Collections.unmodifiableList(parameterMappings));
		}

		public Object parameterObject() {
			return get("parameterObject");
		}

		public Map<String, Object> additionalParameters() {
			return get("additionalParameters");
		}

		@SuppressWarnings("unchecked")
		private <T> T get(String property) {
			return (T) boundSql.getValue(property);
		}

	}

}

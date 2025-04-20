package com.relaxed.extend.mybatis.plus.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * SQL错误拦截器
 * <p>
 * 用于拦截SQL执行过程中的错误，记录错误SQL语句和执行时间。 支持拦截查询、更新和批量操作，当SQL执行出错时，会记录完整的SQL语句和执行耗时。
 * </p>
 */
@Intercepts({
		@Signature(type = StatementHandler.class, method = "query", args = { Statement.class, ResultHandler.class }),
		@Signature(type = StatementHandler.class, method = "update", args = { Statement.class }),
		@Signature(type = StatementHandler.class, method = "batch", args = { Statement.class }) })
@Slf4j
public class SqlErrorInterceptor implements Interceptor, ApplicationContextAware {

	/**
	 * Spring应用上下文
	 */
	private ApplicationContext applicationContext;

	/**
	 * MyBatis的SqlSessionFactory
	 */
	private SqlSessionFactory sqlSessionFactory;

	/**
	 * 拦截SQL执行，记录错误信息
	 * <p>
	 * 当SQL执行出错时，会记录完整的SQL语句和执行耗时。 使用MyBatisPlus的工具类获取完整SQL，包括参数值。
	 * </p>
	 * @param invocation 拦截器调用信息
	 * @return 执行结果
	 * @throws Throwable 执行过程中可能抛出的异常
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();

		long startTime = System.currentTimeMillis();
		Throwable throwable = null;
		try {
			return invocation.proceed();
		}
		catch (Exception e) {
			throwable = e;
			throw e;
		}
		finally {
			if (throwable != null) {
				long endTime = System.currentTimeMillis();
				String completeSql = getCompleteSql(statementHandler);
				long costTime = endTime - startTime;
				log.error("SQL执行出错,耗时:{},完整SQL: \n{}", costTime, completeSql);
			}
		}
	}

	/**
	 * 获取SqlSessionFactory实例
	 * <p>
	 * 使用双重检查锁定模式确保线程安全。
	 * </p>
	 * @return SqlSessionFactory实例
	 */
	public SqlSessionFactory getSqlSessionFactory() {
		if (sqlSessionFactory == null) {
			synchronized (this) {
				if (sqlSessionFactory == null) {
					sqlSessionFactory = this.applicationContext.getBean(SqlSessionFactory.class);
				}
			}
		}
		return sqlSessionFactory;
	}

	/**
	 * 设置Spring应用上下文
	 * @param applicationContext Spring应用上下文
	 * @throws BeansException 如果设置过程中发生异常
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * 获取完整的SQL语句
	 * <p>
	 * 将SQL语句中的占位符替换为实际的参数值，生成可执行的SQL语句。 支持处理Map参数和普通对象参数。
	 * </p>
	 * @param statementHandler StatementHandler实例
	 * @return 完整的SQL语句
	 */
	private String getCompleteSql(StatementHandler statementHandler) {
		BoundSql boundSql = statementHandler.getBoundSql();
		String sql = boundSql.getSql();
		Configuration configuration = this.getSqlSessionFactory().getConfiguration();
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		Object parameterObject = boundSql.getParameterObject();

		if (parameterMappings == null || parameterMappings.isEmpty()) {
			return sql;
		}

		List<Object> paramValues = new ArrayList<>();
		if (parameterObject instanceof Map) {
			Map<?, ?> paramMap = (Map<?, ?>) parameterObject;
			for (ParameterMapping mapping : parameterMappings) {
				String property = mapping.getProperty();
				if (paramMap.containsKey(property)) {
					paramValues.add(paramMap.get(property));
				}
				else if (boundSql.hasAdditionalParameter(property)) {
					paramValues.add(boundSql.getAdditionalParameter(property));
				}
				else {
					paramValues.add(null);
				}
			}
		}
		else {
			if (parameterMappings.size() == 1) {
				paramValues.add(parameterObject);
			}
			else {
				MetaObject metaObject = configuration.newMetaObject(parameterObject);
				for (ParameterMapping mapping : parameterMappings) {
					String property = mapping.getProperty();
					if (metaObject.hasGetter(property)) {
						paramValues.add(metaObject.getValue(property));
					}
					else if (boundSql.hasAdditionalParameter(property)) {
						paramValues.add(boundSql.getAdditionalParameter(property));
					}
					else {
						paramValues.add(null);
					}
				}
			}
		}

		return replacePlaceholders(sql, paramValues);
	}

	/**
	 * 替换SQL语句中的占位符
	 * <p>
	 * 将SQL语句中的问号占位符替换为实际的参数值。 支持处理null值、数字、日期和字符串类型的参数。
	 * </p>
	 * @param sql 原始SQL语句
	 * @param paramValues 参数值列表
	 * @return 替换后的SQL语句
	 */
	private String replacePlaceholders(String sql, List<Object> paramValues) {
		StringBuilder result = new StringBuilder();
		int index = 0;
		int paramIndex = 0;
		int questionMarkIndex;

		while ((questionMarkIndex = sql.indexOf('?', index)) != -1) {
			result.append(sql, index, questionMarkIndex);

			if (paramIndex < paramValues.size()) {
				Object paramValue = paramValues.get(paramIndex++);
				result.append(formatParameter(paramValue));
			}
			else {
				result.append("?");
			}

			index = questionMarkIndex + 1;
		}

		result.append(sql.substring(index));
		return result.toString();
	}

	/**
	 * 格式化参数值
	 * <p>
	 * 根据参数类型进行格式化处理： - null值转换为"NULL" - 数字类型直接转换为字符串 - 日期类型格式化为"yyyy-MM-dd HH:mm:ss" -
	 * 字符串类型添加单引号并转义单引号
	 * </p>
	 * @param paramValue 参数值
	 * @return 格式化后的参数值
	 */
	private String formatParameter(Object paramValue) {
		if (paramValue == null) {
			return "NULL";
		}

		if (paramValue instanceof Number) {
			return paramValue.toString();
		}

		if (paramValue instanceof Date) {
			return "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) paramValue) + "'";
		}

		if (paramValue instanceof String) {
			return "'" + paramValue.toString().replace("'", "''") + "'";
		}

		return "'" + paramValue.toString() + "'";
	}

	/**
	 * 创建代理对象
	 * @param target 目标对象
	 * @return 代理对象
	 */
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	/**
	 * 设置拦截器属性
	 * @param properties 属性配置
	 */
	@Override
	public void setProperties(Properties properties) {
	}

}

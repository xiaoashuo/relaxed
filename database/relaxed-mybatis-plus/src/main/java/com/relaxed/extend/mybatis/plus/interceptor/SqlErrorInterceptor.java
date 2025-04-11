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
 * @author Yakir
 * @Topic SqlErrorInterceptor
 * @Description 拦截错误sql并打印日志
 * @date 2025/4/11 9:25
 * @Version 1.0
 */
@Intercepts({
		@Signature(type = StatementHandler.class, method = "query", args = { Statement.class, ResultHandler.class }),
		@Signature(type = StatementHandler.class, method = "update", args = { Statement.class }),
		@Signature(type = StatementHandler.class, method = "batch", args = { Statement.class }) })
@Slf4j
public class SqlErrorInterceptor implements Interceptor, ApplicationContextAware {

	private ApplicationContext applicationContext;

	private SqlSessionFactory sqlSessionFactory;

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
				// 使用MyBatisPlus 3.5.4.1的工具类获取完整SQL
				String completeSql = getCompleteSql(statementHandler);
				long costTime = endTime - startTime;
				log.error("SQL执行出错,耗时:{},完整SQL: \n{}", costTime, completeSql);
			}
		}
	}

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

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	private String getCompleteSql(StatementHandler statementHandler) {
		BoundSql boundSql = statementHandler.getBoundSql();
		// 获取原始SQL（带?的）
		String sql = boundSql.getSql();
		Configuration configuration = this.getSqlSessionFactory().getConfiguration();
		// 获取参数列表
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		Object parameterObject = boundSql.getParameterObject();

		if (parameterMappings == null || parameterMappings.isEmpty()) {
			return sql;
		}

		// 构建参数值列表
		List<Object> paramValues = new ArrayList<>();
		if (parameterObject instanceof Map) {
			// 处理Map参数
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
			// 处理非Map参数（单个参数）
			if (parameterMappings.size() == 1) {
				paramValues.add(parameterObject);
			}
			else {
				// 处理多个参数（如@Param注解情况）
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

		// 替换SQL中的?为实际参数值
		return replacePlaceholders(sql, paramValues);
	}

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

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
	}

}

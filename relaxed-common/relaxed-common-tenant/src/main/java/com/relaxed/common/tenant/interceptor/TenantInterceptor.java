package com.relaxed.common.tenant.interceptor;

import com.relaxed.common.tenant.exception.TenantException;
import com.relaxed.common.tenant.handler.TenantHandle;
import com.relaxed.common.tenant.manage.DataSchemaManage;
import com.relaxed.common.tenant.parse.SqlParser;
import com.relaxed.common.tenant.utils.PluginUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;

/**
 * @author Yakir
 * @Topic TenantInterceptor
 * @Description
 * @date 2021/7/26 14:55
 * @Version 1.0
 */
@RequiredArgsConstructor
@Slf4j
@Intercepts({
		@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class TenantInterceptor implements Interceptor {

	private final TenantHandle tenantHandle;

	private final DataSchemaManage dataSchemaManage;

	private final SqlParser sqlParser;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		if (!tenantHandle.enable()) {
			return invocation.proceed();
		}
		String currentSchema = tenantHandle.getCurrentSchema();
		// 包含则忽略当前schema
		if (dataSchemaManage.ignore(currentSchema)) {
			return invocation.proceed();
		}
		// 验证通过执行sql语句替换
		Object target = invocation.getTarget();
		StatementHandler sh = (StatementHandler) target;
		PluginUtils.MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);
		MappedStatement ms = mpSh.mappedStatement();
		// mapper 方法全路径
		String mappedStatementId = ms.getId();
		// 忽略指定方法
		if (dataSchemaManage.ignoreMethod(mappedStatementId)) {
			return invocation.proceed();
		}
		// 验证所有schema 里面是否包含指定schema 不包含抛出异常
		if (!dataSchemaManage.validSchema(currentSchema)) {
			throw new TenantException("current schema not valid.");
		}
		// 验证通过执行sql语句替换
		PluginUtils.MPBoundSql mpBs = mpSh.mPBoundSql();
		SqlCommandType sct = ms.getSqlCommandType();
		// 原始sql语句
		String originalSql = mpBs.sql();
		String replaceSql = sqlParser.processSql(originalSql, currentSchema);
		mpBs.sql(replaceSql);
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		}

		return target;
	}

}

package com.relaxed.common.tenant.interceptor;

import com.relaxed.common.tenant.core.Tenant;
import com.relaxed.common.tenant.core.schema.SchemaHandler;
import com.relaxed.common.tenant.core.table.DataScope;
import com.relaxed.common.tenant.core.table.TableHandler;
import com.relaxed.common.tenant.parse.SqlParser;
import com.relaxed.common.tenant.utils.PluginUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.util.List;

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

	private final SchemaHandler schemaHandler;

	private final TableHandler tableHandler;

	private final SqlParser sqlParser;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		boolean enableSchema = schemaHandler.enable();
		boolean enableTable = tableHandler.enable();
		// schema 与 表字段都不开启 跳过
		if (!enableSchema && !enableTable) {
			return invocation.proceed();
		}
		Tenant tenant = new Tenant();

		// 验证通过执行sql语句替换
		Object target = invocation.getTarget();
		StatementHandler sh = (StatementHandler) target;
		PluginUtils.MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);
		MappedStatement ms = mpSh.mappedStatement();
		// mapper 方法全路径
		String mappedStatementId = ms.getId();
		if (enableSchema) {
			String currentSchema = schemaHandler.getCurrentSchema();
			// 忽略指定方法
			if (currentSchema == null || "".equals(currentSchema) || schemaHandler.ignore(currentSchema)
					|| schemaHandler.ignoreMethod(mappedStatementId)) {
				tenant.setSchema(false);
			}
			else {
				// 填充租户
				tenant.setSchema(true);
				tenant.setSchemaName(currentSchema);
			}
		}
		else {
			tenant.setSchema(false);
		}
		// 是否忽略租户列处理
		if (!enableTable || tableHandler.ignore(mappedStatementId)) {
			tenant.setTable(false);
		}
		else {
			tenant.setTable(true);
			List<DataScope> dataScopes = tableHandler.filterDataScopes(mappedStatementId);
			// 再次判断数据域是否为空 为空 则变更为未开启状态
			if (dataScopes == null || dataScopes.isEmpty()) {
				tenant.setTable(false);
			}
			else {
				tenant.setDataScopes(dataScopes);
			}
		}
		// 验证通过执行sql语句替换
		PluginUtils.MPBoundSql mpBs = mpSh.mPBoundSql();
		// 原始sql语句
		String originalSql = mpBs.sql();
		mpBs.sql(sqlParser.processSql(originalSql, tenant));
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

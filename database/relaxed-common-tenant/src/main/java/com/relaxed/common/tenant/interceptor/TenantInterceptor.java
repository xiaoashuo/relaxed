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
 * 租户拦截器 用于拦截SQL语句，根据租户配置对SQL进行动态修改 支持Schema级别和表级别的租户隔离
 *
 * @author Yakir
 */
@RequiredArgsConstructor
@Slf4j
@Intercepts({
		@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class TenantInterceptor implements Interceptor {

	/**
	 * Schema处理器，用于处理数据库Schema级别的租户隔离
	 */
	private final SchemaHandler schemaHandler;

	/**
	 * 表处理器，用于处理表级别的租户数据隔离
	 */
	private final TableHandler tableHandler;

	/**
	 * SQL解析器，用于解析和修改SQL语句
	 */
	private final SqlParser sqlParser;

	/**
	 * 拦截SQL执行，根据租户配置对SQL进行动态修改
	 * @param invocation 拦截器调用对象
	 * @return 执行结果
	 * @throws Throwable 执行异常
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// 检查是否启用Schema和表级别的租户隔离
		boolean enableSchema = schemaHandler.enable();
		boolean enableTable = tableHandler.enable();
		// 如果两者都未启用，则直接执行原始SQL
		if (!enableSchema && !enableTable) {
			return invocation.proceed();
		}
		Tenant tenant = new Tenant();

		// 获取当前执行的SQL语句信息
		Object target = invocation.getTarget();
		StatementHandler sh = (StatementHandler) target;
		PluginUtils.MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);
		MappedStatement ms = mpSh.mappedStatement();
		// 获取Mapper方法的全路径名
		String mappedStatementId = ms.getId();

		// 处理Schema级别的租户隔离
		if (enableSchema) {
			String currentSchema = schemaHandler.getCurrentSchema();
			// 检查是否需要忽略当前Schema或方法
			if (currentSchema == null || "".equals(currentSchema) || schemaHandler.ignore(currentSchema)
					|| schemaHandler.ignoreMethod(mappedStatementId)) {
				tenant.setSchema(false);
			}
			else {
				// 设置Schema信息
				tenant.setSchema(true);
				tenant.setSchemaName(currentSchema);
			}
		}
		else {
			tenant.setSchema(false);
		}

		// 处理表级别的租户数据隔离
		if (!enableTable || tableHandler.ignore(mappedStatementId)) {
			tenant.setTable(false);
		}
		else {
			tenant.setTable(true);
			List<DataScope> dataScopes = tableHandler.filterDataScopes(mappedStatementId);
			// 如果数据域为空，则关闭表级别隔离
			if (dataScopes == null || dataScopes.isEmpty()) {
				tenant.setTable(false);
			}
			else {
				tenant.setDataScopes(dataScopes);
			}
		}

		// 根据租户配置修改SQL语句
		PluginUtils.MPBoundSql mpBs = mpSh.mPBoundSql();
		String originalSql = mpBs.sql();
		mpBs.sql(sqlParser.processSql(originalSql, tenant));
		return invocation.proceed();
	}

	/**
	 * 插件包装方法，用于包装StatementHandler
	 * @param target 目标对象
	 * @return 包装后的对象
	 */
	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		}
		return target;
	}

}

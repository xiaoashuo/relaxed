package com.relaxed.common.datascope.interceptor;

import com.relaxed.common.datascope.DataScope;
import com.relaxed.common.datascope.handler.DataPermissionHandler;
import com.relaxed.common.datascope.holder.DataScopeMatchNumHolder;
import com.relaxed.common.datascope.holder.MappedStatementIdsWithoutDataScope;
import com.relaxed.common.datascope.processor.DataScopeSqlProcessor;
import com.relaxed.common.datascope.util.PluginUtils;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.util.List;

/**
 * 数据权限拦截器
 * <p>
 * 拦截 SQL 语句执行，根据配置的数据权限规则对 SQL 进行修改， 实现数据权限控制。支持 SELECT、INSERT、UPDATE、DELETE 操作。
 */
@RequiredArgsConstructor
@Intercepts({
		@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class DataPermissionInterceptor implements Interceptor {

	private final DataScopeSqlProcessor dataScopeSqlProcessor;

	private final DataPermissionHandler dataPermissionHandler;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// 第一版，测试用
		Object target = invocation.getTarget();
		StatementHandler sh = (StatementHandler) target;
		PluginUtils.MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);
		MappedStatement ms = mpSh.mappedStatement();
		SqlCommandType sct = ms.getSqlCommandType();
		PluginUtils.MPBoundSql mpBs = mpSh.mPBoundSql();
		String mappedStatementId = ms.getId();

		// 获取当前需要控制的 dataScope 集合
		List<DataScope> dataScopes = dataPermissionHandler.filterDataScopes(mappedStatementId);
		if (dataScopes == null || dataScopes.isEmpty()) {
			return invocation.proceed();
		}

		// 根据用户权限判断是否需要拦截，例如管理员可以查看所有，则直接放行
		if (dataPermissionHandler.ignorePermissionControl(dataScopes, mappedStatementId)) {
			return invocation.proceed();
		}

		try {
			// 创建 matchNumTreadLocal
			DataScopeMatchNumHolder.initMatchNum();
			// 根据 DataScopes 进行数据权限的 sql 处理
			if (sct == SqlCommandType.SELECT) {
				mpBs.sql(dataScopeSqlProcessor.parserSingle(mpBs.sql(), dataScopes));
			}
			else if (sct == SqlCommandType.INSERT || sct == SqlCommandType.UPDATE || sct == SqlCommandType.DELETE) {
				mpBs.sql(dataScopeSqlProcessor.parserMulti(mpBs.sql(), dataScopes));
			}
			// 如果解析后发现当前 mappedStatementId 对应的 sql，没有任何数据权限匹配，则记录下来，后续可以直接跳过不解析
			if (DataScopeMatchNumHolder.getMatchNum() == 0) {
				MappedStatementIdsWithoutDataScope.addToWithoutSet(dataScopes, mappedStatementId);
			}
		}
		finally {
			DataScopeMatchNumHolder.remove();
		}

		// 执行 sql
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

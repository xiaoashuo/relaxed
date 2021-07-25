package com.relaxed.common.datascope.interceptor;

import com.relaxed.common.datascope.DataScope;
import com.relaxed.common.datascope.handler.DataPermissionHandler;
import com.relaxed.common.datascope.parse.DataSqlParse;
import com.relaxed.common.datascope.utils.PluginUtils;
import lombok.RequiredArgsConstructor;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * @author Yakir
 * @Topic DataPermissionInterceptor
 * @Description
 * @date 2021/7/25 13:36
 * @Version 1.0
 */
@RequiredArgsConstructor
@Intercepts({
		@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class DataPermissionInterceptor implements Interceptor {

	private final DataSqlParse dataSqlParse;

	private final DataPermissionHandler dataPermissionHandler;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object target = invocation.getTarget();

		StatementHandler sh = (StatementHandler) target;
		PluginUtils.MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);
		MappedStatement ms = mpSh.mappedStatement();
		PluginUtils.MPBoundSql mpBs = mpSh.mPBoundSql();
		SqlCommandType sct = ms.getSqlCommandType();
		// mapper 方法全路径
		String mappedStatementId = ms.getId();
		// 若当前方法 不需要处理数据权限则直接放行
		if (dataPermissionHandler.ignore(mappedStatementId)) {
			return invocation.proceed();
		}
		// 对应mapper id 过滤出来的数据域为空
		List<DataScope> dataScopes = dataPermissionHandler.filterDataScopes(mappedStatementId);
		if (dataScopes == null || dataScopes.isEmpty()) {
			return invocation.proceed();
		}
		// 根据 DataScopes 进行数据权限的 sql 处理
		if (sct == SqlCommandType.SELECT) {
			mpBs.sql(dataSqlParse.parserSingle(mpBs.sql(), dataScopes));
		}
		else if (sct == SqlCommandType.INSERT || sct == SqlCommandType.UPDATE || sct == SqlCommandType.DELETE) {
			mpBs.sql(dataSqlParse.parserMulti(mpBs.sql(), dataScopes));
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

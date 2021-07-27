package com.relaxed.common.tenant.parse;

import com.relaxed.common.tenant.exception.TenantException;
import com.relaxed.common.tenant.handler.Tenant;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.deparser.StatementDeParser;

/**
 * @author Yakir
 * @Topic SqlParse
 * @Description
 * @date 2021/7/26 18:02
 * @Version 1.0
 */
@Slf4j
public abstract class SqlParser {

	public String processSql(String originalSql, Object obj) {
		try {
			Statement stmt = CCJSqlParserUtil.parse(originalSql);
			return processStatement(stmt, obj, originalSql);
		}
		catch (JSQLParserException e) {
			throw new TenantException(String.format("Failed to process, Error SQL: %s", originalSql), e);
		}
	}

	/**
	 * 处理sql 映射java片段
	 * @author yakir
	 * @date 2021/7/27 17:25
	 * @param statement
	 * @param obj tenant 租户信息
	 * @param sql 原始sql
	 * @return java.lang.String
	 */
	protected String processStatement(Statement statement, Object obj, String sql) {
		if (log.isDebugEnabled()) {
			log.info("start handle sql  {}", sql);
		}
		Tenant tenant = (Tenant) obj;
		StringBuilder stringBuffer = new StringBuilder();
		StatementDeParser statementDeParser = getStatementDeParser(stringBuffer, tenant);
		statement.accept(statementDeParser);
		String replaceSql = stringBuffer.toString();
		if (log.isDebugEnabled()) {
			log.info("end handle sql ,after handle sql  {}", replaceSql);
		}
		return replaceSql;
	}

	/**
	 * 根据builder 创建
	 * @author yakir
	 * @date 2021/7/27 17:21
	 * @param builder
	 * @param tenant 租户内信息当前状态
	 * @return net.sf.jsqlparser.util.deparser.StatementDeParser
	 */
	protected StatementDeParser getStatementDeParser(StringBuilder builder, Tenant tenant) {
		throw new TenantException("statementDeParser must be provide");
	}

}

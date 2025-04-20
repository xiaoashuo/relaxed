package com.relaxed.common.tenant.parse;

import com.relaxed.common.tenant.exception.TenantException;
import com.relaxed.common.tenant.core.Tenant;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.deparser.StatementDeParser;

/**
 * SQL解析器抽象类 用于解析SQL语句并根据租户信息进行动态修改 支持Schema级别和表级别的租户隔离
 *
 * @author Yakir
 */
@Slf4j
public abstract class SqlParser {

	/**
	 * 处理SQL语句 解析原始SQL并根据租户信息进行修改
	 * @param originalSql 原始SQL语句
	 * @param obj 租户信息对象
	 * @return 处理后的SQL语句
	 */
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
	 * 处理SQL语句对象 将SQL语句对象转换为字符串，并根据租户信息进行修改
	 * @param statement SQL语句对象
	 * @param obj 租户信息对象
	 * @param sql 原始SQL语句
	 * @return 处理后的SQL语句
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
	 * 获取SQL语句解析器 子类需要实现此方法，提供具体的SQL解析器实现
	 * @param builder SQL语句构建器
	 * @param tenant 租户信息
	 * @return SQL语句解析器
	 */
	protected StatementDeParser getStatementDeParser(StringBuilder builder, Tenant tenant) {
		throw new TenantException("statementDeParser must be provide");
	}

}

package com.relaxed.common.datascope.parser;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

/**
 * SQL 解析器支持类
 * <p>
 * 基于 JSQLParser 实现的 SQL 解析支持类，提供单条 SQL 和多条 SQL 的解析能力。 支持解析 SELECT、INSERT、UPDATE、DELETE 等
 * SQL 语句。
 */
@Slf4j
public abstract class JsqlParserSupport {

	/**
	 * 解析单条 SQL 语句
	 * <p>
	 * 使用 JSQLParser 解析单条 SQL 语句，并应用相应的处理逻辑。
	 * @param sql 待解析的 SQL 语句
	 * @param obj 处理参数
	 * @return 处理后的 SQL 语句
	 */
	public String parserSingle(String sql, Object obj) {
		try {
			Statement statement = CCJSqlParserUtil.parse(sql);
			return processParser(statement, 0, sql, obj);
		}
		catch (JSQLParserException e) {
			throw new RuntimeException(String.format("Failed to process, Error SQL: %s", sql), e);
		}
	}

	/**
	 * 解析多条 SQL 语句
	 * <p>
	 * 使用 JSQLParser 解析多条 SQL 语句，并分别应用相应的处理逻辑。 多条语句之间使用分号分隔。
	 * @param sql 待解析的 SQL 语句
	 * @param obj 处理参数
	 * @return 处理后的 SQL 语句
	 */
	public String parserMulti(String sql, Object obj) {
		try {
			// fixed github pull/295
			StringBuilder sb = new StringBuilder();
			Statements statements = CCJSqlParserUtil.parseStatements(sql);
			int i = 0;
			for (Statement statement : statements.getStatements()) {
				if (i > 0) {
					sb.append(";");
				}
				sb.append(processParser(statement, i, sql, obj));
				i++;
			}
			return sb.toString();
		}
		catch (JSQLParserException e) {
			throw new RuntimeException(String.format("Failed to process, Error SQL: %s", sql), e);
		}
	}

	/**
	 * 执行 SQL 解析
	 * <p>
	 * 根据 SQL 语句类型调用相应的处理方法。
	 * @param statement JSQLParser Statement 对象
	 * @param index 当前处理的语句索引
	 * @param sql 原始 SQL 语句
	 * @param obj 处理参数
	 * @return 处理后的 SQL 语句
	 */
	protected String processParser(Statement statement, int index, String sql, Object obj) {
		if (log.isDebugEnabled()) {
			log.debug("SQL to parse, SQL: " + sql);
		}
		if (statement instanceof Insert) {
			this.processInsert((Insert) statement, index, sql, obj);
		}
		else if (statement instanceof Select) {
			this.processSelect((Select) statement, index, sql, obj);
		}
		else if (statement instanceof Update) {
			this.processUpdate((Update) statement, index, sql, obj);
		}
		else if (statement instanceof Delete) {
			this.processDelete((Delete) statement, index, sql, obj);
		}
		sql = statement.toString();
		if (log.isDebugEnabled()) {
			log.debug("parse the finished SQL: " + sql);
		}
		return sql;
	}

	/**
	 * 处理 INSERT 语句
	 * <p>
	 * 子类需要实现此方法以处理 INSERT 语句。
	 * @param insert INSERT 语句对象
	 * @param index 当前处理的语句索引
	 * @param sql 原始 SQL 语句
	 * @param obj 处理参数
	 */
	protected void processInsert(Insert insert, int index, String sql, Object obj) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 处理 DELETE 语句
	 * <p>
	 * 子类需要实现此方法以处理 DELETE 语句。
	 * @param delete DELETE 语句对象
	 * @param index 当前处理的语句索引
	 * @param sql 原始 SQL 语句
	 * @param obj 处理参数
	 */
	protected void processDelete(Delete delete, int index, String sql, Object obj) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 处理 UPDATE 语句
	 * <p>
	 * 子类需要实现此方法以处理 UPDATE 语句。
	 * @param update UPDATE 语句对象
	 * @param index 当前处理的语句索引
	 * @param sql 原始 SQL 语句
	 * @param obj 处理参数
	 */
	protected void processUpdate(Update update, int index, String sql, Object obj) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 处理 SELECT 语句
	 * <p>
	 * 子类需要实现此方法以处理 SELECT 语句。
	 * @param select SELECT 语句对象
	 * @param index 当前处理的语句索引
	 * @param sql 原始 SQL 语句
	 * @param obj 处理参数
	 */
	protected void processSelect(Select select, int index, String sql, Object obj) {
		throw new UnsupportedOperationException();
	}

}

package com.relaxed.common.tenant.parse;

import com.relaxed.common.tenant.exception.TenantException;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;
import net.sf.jsqlparser.util.deparser.UpdateDeParser;

import java.util.List;

/**
 * @author Yakir
 * @Topic SqlParse
 * @Description
 * @date 2021/7/26 18:02
 * @Version 1.0
 */
@Slf4j
public abstract class SqlParser {

	public String processSql(String originalSql, String currentSchema) {
		log.info("original sql {}", originalSql);
		try {
			// 需要sql校验。
			String schemeName = String.format("`%s`", currentSchema);
			Statement stmt = CCJSqlParserUtil.parse(originalSql);
			return processStatement(stmt, schemeName, originalSql);
		}
		catch (JSQLParserException e) {
			throw new TenantException(String.format("Failed to process, Error SQL: %s", originalSql), e);
		}
	}

	protected String processStatement(Statement statement, String schemeName, String sql) {
		if (statement instanceof Insert) {
			Insert insert = (Insert) statement;
			return doInsert(insert, schemeName);
		}
		else if (statement instanceof Update) {
			Update update = (Update) statement;
			return doUpdate(update, schemeName);
		}
		else if (statement instanceof Delete) {
			Delete delete = (Delete) statement;
			return doDelete(delete, schemeName);
		}
		else if (statement instanceof Select) {
			Select select = (Select) statement;
			return doSelect(select, schemeName);
		}
		sql = statement.toString();
		if (log.isDebugEnabled()) {
			log.debug("parse the finished SQL: " + sql);
		}
		return sql;
	}

	/**
	 * 插入操作
	 * @author yakir
	 * @date 2021/7/26 21:20
	 * @param insert
	 * @param schemaName
	 * @return java.lang.String
	 */
	protected String doInsert(Insert insert, String schemaName) {
		// 是否设置库名
		Table table = insert.getTable();
		table.setSchemaName(schemaName);
		insert.setTable(table);
		Select select = insert.getSelect();
		if (select != null) {
			doSelect(select, schemaName);
		}
		return insert.toString();
	}

	/**
	 * 查询解析器
	 * @author yakir
	 * @date 2021/7/26 21:19
	 * @param select
	 * @param schemeName
	 * @return java.lang.String
	 */
	protected String doSelect(Select select, String schemeName) {
		processPlainSelect((PlainSelect) select.getSelectBody());
		StringBuilder buffer = new StringBuilder();
		ExpressionDeParser expressionDeParser = new ExpressionDeParser();
		SQLParserSelect parser = new SQLParserSelect(expressionDeParser, buffer);
		parser.setSchemaName(schemeName);
		expressionDeParser.setSelectVisitor(parser);
		expressionDeParser.setBuffer(buffer);
		select.getSelectBody().accept(parser);

		return buffer.toString();
	}

	/**
	 * 更新操作
	 * @author yakir
	 * @date 2021/7/26 21:20
	 * @param update
	 * @param schemaName
	 * @return java.lang.String
	 */
	protected String doUpdate(Update update, String schemaName) {

		// 追加库名
		StringBuilder buffer = new StringBuilder();
		Table tb = update.getTable();
		tb.setSchemaName(schemaName);
		update.setTable(tb);
		// 处理from
		FromItem fromItem = update.getFromItem();
		if (fromItem != null) {
			Table tf = (Table) fromItem;
			tf.setSchemaName(schemaName);
		}
		// 处理join
		List<Join> joins = update.getJoins();
		if (joins != null && joins.size() > 0) {
			for (Object object : joins) {
				Join t = (Join) object;
				Table rightItem = (Table) t.getRightItem();
				rightItem.setSchemaName(schemaName);
			}
		}
		List<Join> startJoins = update.getStartJoins();
		if (startJoins != null && startJoins.size() > 0) {
			for (Join startJoin : startJoins) {
				Table rightItem = (Table) startJoin.getRightItem();
				rightItem.setSchemaName(schemaName);
			}
		}
		ExpressionDeParser expressionDeParser = new ExpressionDeParser();
		UpdateDeParser p = new UpdateDeParser(expressionDeParser, null, buffer);
		expressionDeParser.setBuffer(buffer);
		p.deParse(update);

		return update.toString();
	}

	/**
	 * 删除操作
	 * @author yakir
	 * @date 2021/7/26 21:20
	 * @param delete
	 * @param schemaName
	 * @return java.lang.String
	 */
	protected String doDelete(Delete delete, String schemaName) {
		// 设置库名
		Table t = delete.getTable();
		t.setSchemaName(schemaName);
		delete.setTable(t);
		Expression where = delete.getWhere();
		processWhereSubSelect(where, schemaName);
		return delete.toString();
	}

	/**
	 * 处理where条件内的子查询
	 * <p>
	 * 支持如下: 1. in 2. = 3. > 4. < 5. >= 6. <= 7. <> 8. EXISTS 9. NOT EXISTS
	 * <p>
	 * 前提条件: 1. 子查询必须放在小括号中 2. 子查询一般放在比较操作符的右边
	 * @param where where 条件
	 */
	protected void processWhereSubSelect(Expression where, String schema) {
		if (where == null) {
			return;
		}
		if (where.toString().indexOf("SELECT") > 0) {
			// 有子查询

			if (where instanceof BinaryExpression) {
				// 比较符号 , and , or , 等等
				BinaryExpression expression = (BinaryExpression) where;
				processWhereSubSelect(expression.getLeftExpression(), schema);
				processWhereSubSelect(expression.getRightExpression(), schema);
			}
			else if (where instanceof InExpression) {
				// in
				InExpression expression = (InExpression) where;
				ItemsList itemsList = expression.getRightItemsList();
				if (itemsList instanceof SubSelect) {
					SelectBody selectBody = ((SubSelect) itemsList).getSelectBody();
					processSelectBody(selectBody, schema);
				}
			}
			else if (where instanceof ExistsExpression) {
				// exists
				ExistsExpression expression = (ExistsExpression) where;
				processWhereSubSelect(expression.getRightExpression(), schema);
			}
			else if (where instanceof NotExpression) {
				// not exists
				NotExpression expression = (NotExpression) where;
				processWhereSubSelect(expression.getExpression(), schema);
			}
			else if (where instanceof Parenthesis) {
				Parenthesis expression = (Parenthesis) where;
				processWhereSubSelect(expression.getExpression(), schema);
			}
		}
	}

	protected void processSelectBody(SelectBody selectBody, String schema) {
		if (selectBody == null) {
			return;
		}
		if (selectBody instanceof PlainSelect) {
			PlainSelect plainSelect = (PlainSelect) selectBody;
			FromItem fromItem = plainSelect.getFromItem();
			if (fromItem != null) {
				Table table = (Table) fromItem;
				table.setSchemaName(schema);
			}
		}
	}

	protected static class SQLParserSelect extends SelectDeParser {

		private String schemaName;

		public SQLParserSelect(ExpressionVisitor expressionVisitor, StringBuilder buffer) {
			super(expressionVisitor, buffer);
		}

		public String getSchemaName() {
			return schemaName;
		}

		public void setSchemaName(String schemaName) {
			this.schemaName = schemaName;
		}

		@Override
		public void visit(Table tableName) {
			tableName.setSchemaName(schemaName);
			StringBuilder buffer = getBuffer();
			buffer.append(tableName.getFullyQualifiedName());
			Pivot pivot = tableName.getPivot();
			if (pivot != null) {
				pivot.accept(this);
			}
			Alias alias = tableName.getAlias();
			if (alias != null) {
				buffer.append(alias);
			}
		}

	}

	/**
	 * 处理 PlainSelect
	 */
	protected void processPlainSelect(PlainSelect plainSelectStatement) {
		processPlainSelect(plainSelectStatement, false);
	}

	/**
	 * 处理 PlainSelect
	 * @param plainSelectStatement ignore
	 * @param addColumn 是否添加租户列,insert into select语句中需要
	 */
	protected void processPlainSelect(PlainSelect plainSelectStatement, boolean addColumn) {
		FromItem fromItem = plainSelectStatement.getFromItem();
		if (fromItem instanceof Table) {
			Table fromTable = (Table) fromItem;

		}

	}

}

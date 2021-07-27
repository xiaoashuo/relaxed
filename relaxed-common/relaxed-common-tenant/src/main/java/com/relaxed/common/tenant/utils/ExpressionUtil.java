package com.relaxed.common.tenant.utils;

import com.relaxed.common.tenant.handler.Tenant;
import com.relaxed.common.tenant.handler.table.DataScope;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.update.Update;

import java.util.List;
import java.util.Objects;

/**
 * @author Yakir
 * @Topic ExpressionUtil
 * @Description
 * @date 2021/7/27 16:45
 * @Version 1.0
 */
public class ExpressionUtil {

	/**
	 * 填充表名schema
	 * @author yakir
	 * @date 2021/7/27 17:46
	 * @param table
	 * @param tenant
	 */
	public static void fillTableSchema(Table table, Tenant tenant) {
		if (tenant.isSchema()) {
			table.setSchemaName(tenant.getSchemaName());
		}
	}

	/**
	 * 填充表名schema
	 * @param statement
	 * @param tenant
	 */
	public static void fillTableSchema(Statement statement, Tenant tenant) {

		if (statement instanceof Insert) {
			Insert insert = (Insert) statement;
			if (tenant.isSchema()) {
				Table table = insert.getTable();
				table.setSchemaName(tenant.getSchemaName());
				insert.setTable(table);
			}
		}
		else if (statement instanceof Update) {
			Update update = (Update) statement;
			if (tenant.isSchema()) {
				Table table = update.getTable();
				table.setSchemaName(tenant.getSchemaName());
				update.setTable(table);
				List<Join> startJoins = update.getStartJoins();
				if (startJoins != null && startJoins.size() > 0) {
					for (Join startJoin : startJoins) {
						Table rightItem = (Table) startJoin.getRightItem();
						rightItem.setSchemaName(tenant.getSchemaName());
					}
				}
			}
		}
		else if (statement instanceof Delete) {
			Delete delete = (Delete) statement;
			if (tenant.isSchema()) {
				Table table = delete.getTable();
				table.setSchemaName(tenant.getSchemaName());
				delete.setTable(table);
			}

		}

	}

	/**
	 * 处理无where 表达式
	 * @param table
	 * @param tenant
	 * @param builder
	 * @return
	 */
	public static Expression processNoWhereExpression(Table table, Tenant tenant, StringBuilder builder) {
		if (tenant.isTable()) {
			builder.append(" WHERE ");
			return ExpressionUtil.injectExpressionNoWhere(table, tenant);
		}
		return null;
	}

	/**
	 * 注入 无where 表达式
	 * @author yakir
	 * @date 2021/7/27 16:46
	 * @param table
	 * @param tenant
	 * @return net.sf.jsqlparser.expression.Expression
	 */
	public static Expression injectExpressionNoWhere(Table table, Tenant tenant) {
		String tableName = table.getName();
		List<DataScope> dataScopes = tenant.getDataScopes();
		Expression dataFilterExpression = dataScopes.stream().filter(x -> x.getTableNames().contains(tableName))
				.map(x -> x.getExpression(tableName, table.getAlias())).filter(Objects::nonNull)
				.reduce(AndExpression::new).orElse(null);
		return dataFilterExpression;
	}

	/**
	 * 根据 DataScope ，将数据过滤的表达式注入原本的 where/or 条件
	 * @param currentExpression Expression where/or
	 * @param table 表信息
	 * @return 修改后的 where/or 条件
	 */
	public static Expression injectExpression(Expression currentExpression, Table table, Tenant tenant) {
		String tableName = table.getName();
		// 不开启字段 处理 及直接返回当前表达式
		if (!tenant.isTable()) {
			return currentExpression;
		}
		List<DataScope> dataScopes = tenant.getDataScopes();

		Expression dataFilterExpression = dataScopes.stream().filter(x -> x.getTableNames().contains(tableName))
				.map(x -> x.getExpression(tableName, table.getAlias())).filter(Objects::nonNull)
				.reduce(AndExpression::new).orElse(null);
		if (currentExpression == null) {
			return dataFilterExpression;
		}
		if (dataFilterExpression == null) {
			return currentExpression;
		}
		if (currentExpression instanceof OrExpression) {
			return new AndExpression(new Parenthesis(currentExpression), dataFilterExpression);
		}
		else {
			return new AndExpression(currentExpression, dataFilterExpression);
		}
	}

}

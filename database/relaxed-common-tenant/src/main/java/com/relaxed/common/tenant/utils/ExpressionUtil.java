package com.relaxed.common.tenant.utils;

import com.relaxed.common.tenant.core.Tenant;
import com.relaxed.common.tenant.core.table.DataScope;
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
 * SQL表达式工具类 提供SQL表达式处理相关的工具方法 包括表名Schema填充、条件表达式注入等功能
 *
 * @author Yakir
 */
public class ExpressionUtil {

	/**
	 * 填充表的Schema名称 根据租户信息设置表的Schema名称
	 * @param table 表对象
	 * @param tenant 租户信息
	 */
	public static void fillTableSchema(Table table, Tenant tenant) {
		if (tenant.isSchema()) {
			table.setSchemaName(tenant.getSchemaName());
		}
	}

	/**
	 * 填充SQL语句中所有表的Schema名称 支持INSERT、UPDATE、DELETE语句的表名Schema填充
	 * @param statement SQL语句对象
	 * @param tenant 租户信息
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
	 * 处理无WHERE条件的表达式注入 为表添加WHERE条件和租户数据过滤表达式
	 * @param table 表对象
	 * @param tenant 租户信息
	 * @param builder SQL语句构建器
	 * @return 数据过滤表达式
	 */
	public static Expression injectExpressionNoWhere(Table table, Tenant tenant, StringBuilder builder) {
		if (tenant.isTable()) {
			builder.append(" WHERE ");
			return injectExpressionNoWhere(table, tenant);
		}
		return null;
	}

	/**
	 * 注入无WHERE条件的数据过滤表达式 根据表名和租户信息生成数据过滤表达式
	 * @param table 表对象
	 * @param tenant 租户信息
	 * @return 数据过滤表达式
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
	 * 注入数据过滤表达式到现有WHERE/OR条件 将租户数据过滤表达式与现有条件组合
	 * @param currentExpression 当前WHERE/OR条件表达式
	 * @param table 表对象
	 * @param tenant 租户信息
	 * @return 组合后的条件表达式
	 */
	public static Expression injectExpression(Expression currentExpression, Table table, Tenant tenant) {
		String tableName = table.getName();
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

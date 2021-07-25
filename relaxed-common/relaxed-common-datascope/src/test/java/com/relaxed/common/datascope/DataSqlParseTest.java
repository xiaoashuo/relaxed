package com.relaxed.common.datascope;

import com.relaxed.common.datascope.handler.AbstractDataPermissionHandler;
import com.relaxed.common.datascope.handler.DataPermissionHandler;
import com.relaxed.common.datascope.parse.DataSqlParse;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @author Yakir
 * @Topic JsqlParseTest
 * @Description
 * @date 2021/7/25 14:46
 * @Version 1.0
 */
public class DataSqlParseTest {

	@Test
	public void testJsql() {
		DataScope dataScope = new DataScope() {
			final String columnId = "order_id";

			@Override
			public String getResource() {
				return "order";
			}

			@Override
			public Collection<String> getTableNames() {
				Set<String> tableNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
				tableNames.addAll(Arrays.asList("t_order", "t_order_info"));
				return tableNames;
			}

			@Override
			public Expression getExpression(String tableName, Alias tableAlias) {
				Column column = new Column(tableAlias == null ? columnId : tableAlias.getName() + "." + columnId);
				ExpressionList expressionList = new ExpressionList();
				expressionList.setExpressions(Arrays.asList(new StringValue("1"), new StringValue("2")));
				return new InExpression(column, expressionList);
			}
		};

		List<DataScope> dataScopes = new ArrayList<>();
		dataScopes.add(dataScope);

		DataPermissionHandler dataPermissionHandler = new AbstractDataPermissionHandler(dataScopes) {
			@Override
			public boolean ignore(String mappedStatementId) {
				return false;
			}
		};
		DataSqlParse dataScopeSqlProcessor = new DataSqlParse();

		// DataScopeHolder.putDataScope("order", dataScope);

		String sql = "select o.order_id,o.order_name,oi.order_price "
				+ "from t_ORDER o left join t_order_info oi on o.order_id = oi.order_id "
				+ "where oi.order_price > 100";

		String parseSql = dataScopeSqlProcessor.parserSingle(sql, dataPermissionHandler.dataScopes());
		System.out.println(parseSql);

		String trueSql = "SELECT o.order_id, o.order_name, oi.order_price FROM t_ORDER o LEFT JOIN t_order_info oi ON o.order_id = oi.order_id AND oi.order_id IN ('1', '2') WHERE oi.order_price > 100 AND o.order_id IN ('1', '2')";
		Assert.isTrue(trueSql.equals(parseSql), "sql 数据权限解析异常");

	}

}

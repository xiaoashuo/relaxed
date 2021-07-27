package com.relaxed.common.tenant.interceptor;

import com.relaxed.common.tenant.core.Tenant;
import com.relaxed.common.tenant.core.schema.SchemaHandler;
import com.relaxed.common.tenant.core.table.DataScope;
import com.relaxed.common.tenant.parse.DefaultSqlParser;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author Yakir
 * @Topic TenantInterceptorTest
 * @Description
 * @date 2021/7/26 18:21
 * @Version 1.0
 */

public class TenantInterceptorTest {

	public class DefaultSchemaHandler implements SchemaHandler {

		List<String> schemas = new ArrayList<String>() {
			{
				set(0, "db1");
				set(1, "db2");
			}
		};

		@Override
		public boolean enable() {
			return false;
		}

		@Override
		public List<String> getAllSchemas() {
			return schemas;
		}

		@Override
		public boolean ignore(String schemaName) {
			if (schemaName == null || "".equals(schemaName)) {
				return true;
			}
			return false;
		}

		@Override
		public boolean ignoreMethod(String mapperId) {
			return false;
		}

		@Override
		public String getCurrentSchema() {
			return "db1";
		}

	}

	@Test
	public void testSql() throws JSQLParserException {
		DefaultSqlParser defaultSqlParser = new DefaultSqlParser();
		// 数据域
		DataScope dataScope = new DataScope() {
			final String columnId = "order_id";

			@Override
			public String getResource() {
				return columnId;
			}

			@Override
			public Collection<String> getTableNames() {
				Set<String> tableNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
				tableNames.addAll(Arrays.asList("t_user", "address", "t_bat"));
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
		Tenant tenant = new Tenant();
		tenant.setSchema(true);
		tenant.setSchemaName("db1");
		tenant.setTable(true);
		tenant.setDataScopes(dataScopes);
		// String sql = "select * from user u left join address a on u.id=a.user_id where
		// u.id in (select id from t_bat tb )";
		String sql = "SELECT u.id, u.tenant_id, u.username FROM t_user u ";
		// String sql="select * from user u left join address a on u.id=a.user_id where
		// u.id in (1,2 )";
		// String sql="select (select id from user) info,* from user u left join address a
		// on u.id=a.user_id where u.id in (select id from t_bat tb )";
		String result = defaultSqlParser.processSql(sql, tenant);

		// String insertSql="insert into user values(1,2)";
		// String insertResult = defaultSqlParser.processSql(insertSql, tenant);
		// System.out.println(insertResult);
		//
		// String insertSql = "insert into user select * from address ";
		// String insertResult = defaultSqlParser.processSql(insertSql, tenant);
		// System.out.println(insertResult);
		// String updateSql="update user u left join address a on u.id=a.user_id set
		// u.username=3 where u.id in (select id from t_bat t )";
		// String updateResult = defaultSqlParser.processSql(updateSql, tenant);
		// System.out.println(updateResult);

		// String updateSql1="update user u,address a set u.username=3 where
		// u.id=a.user_id and u.id in (1,2)";
		// String updateResult1 = defaultSqlParser.processSql(updateSql1, tenant);
		// System.out.println(updateResult1);

		// String deleteSql="delete from user u where u.id=1 and u.user_id in (select id
		// from address a where a.address like '北京')";
		// String deleteResult = defaultSqlParser.processSql(deleteSql, tenant);
		// System.out.println(deleteResult);
		// String deleteSql = "delete from user where u.id=a.user_id ";
		// String deleteResult = defaultSqlParser.processSql(deleteSql, "db1");
		// System.out.println(deleteResult);
	}

}
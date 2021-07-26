package com.relaxed.common.tenant.interceptor;

import com.relaxed.common.tenant.parse.DefaultSqlParser;
import com.relaxed.common.tenant.parse.SqlParser;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Yakir
 * @Topic TenantInterceptorTest
 * @Description
 * @date 2021/7/26 18:21
 * @Version 1.0
 */

public class TenantInterceptorTest {

	@Test
	public void testSql() throws JSQLParserException {
		DefaultSqlParser defaultSqlParser = new DefaultSqlParser();
		// String sql="select * from user u left join address a on u.id=a.user_id where
		// u.id in (select id from t_bat tb)";
		// String result = SqlParser.processSql(sql, "db1");
		// System.out.println(result);

		// String insertSql="insert into user values(1,2)";
		// String insertResult = SqlParser.processSql(insertSql, "db1");
		// System.out.println(insertResult);
		//
		String insertSql = "insert into user  select * from address ";
		String insertResult = defaultSqlParser.processSql(insertSql, "db1");
		System.out.println(insertResult);
		// String updateSql="update user u left join address a on u.id=a.user_id set
		// u.username=3 where u.id in (1,2)";
		// String updateResult = SqlParser.processSql(updateSql, "db1");
		// System.out.println(updateResult);

		// String updateSql1="update user u,address a set u.username=3 where
		// u.id=a.user_id and u.id in (1,2)";
		// String updateResult1 = SqlParser.processSql(updateSql1, "db1");
		// System.out.println(updateResult1);

		// String deleteSql="delete from user where u.id=a.user_id and u.id in (select id
		// from user)";
		// String deleteResult = SqlParser.processSql(deleteSql, "db1");
		// System.out.println(deleteResult);
		// String deleteSql = "delete from user where u.id=a.user_id ";
		// String deleteResult = defaultSqlParser.processSql(deleteSql, "db1");
		// System.out.println(deleteResult);
	}

}
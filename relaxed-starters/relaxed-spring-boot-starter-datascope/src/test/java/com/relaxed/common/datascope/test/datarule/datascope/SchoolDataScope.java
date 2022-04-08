package com.relaxed.common.datascope.test.datarule.datascope;

import com.relaxed.common.datascope.DataScope;
import com.relaxed.common.datascope.test.datarule.user.LoginUser;
import com.relaxed.common.datascope.test.datarule.user.LoginUserHolder;
import com.relaxed.common.datascope.util.CollectionUtils;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hccake
 */
public class SchoolDataScope implements DataScope {

	public static final String RESOURCE_NAME = "school";

	final String columnId = "school_name";

	@Override
	public String getResource() {
		return RESOURCE_NAME;
	}

	@Override
	public Collection<String> getTableNames() {
		Set<String> tableNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		tableNames.addAll(Collections.singletonList("h2student"));
		return tableNames;
	}

	@Override
	public Expression getExpression(String tableName, Alias tableAlias) {
		LoginUser loginUser = LoginUserHolder.get();
		if (loginUser == null || CollectionUtils.isEmpty(loginUser.getSchoolNameList())) {
			// 永不满足
			return new EqualsTo(new LongValue(1), new LongValue(2));
		}

		// 提取当前登录用户拥有的学校权限
		List<Expression> list = loginUser.getSchoolNameList().stream().map(StringValue::new)
				.collect(Collectors.toList());
		Column column = new Column(tableAlias == null ? columnId : tableAlias.getName() + "." + columnId);
		ExpressionList expressionList = new ExpressionList();
		expressionList.setExpressions(list);
		return new InExpression(column, expressionList);
	}

}

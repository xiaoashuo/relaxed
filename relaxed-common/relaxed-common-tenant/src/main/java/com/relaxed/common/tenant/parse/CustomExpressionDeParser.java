package com.relaxed.common.tenant.parse;

import com.relaxed.common.tenant.handler.Tenant;
import com.relaxed.common.tenant.handler.table.DataScope;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * @author Yakir
 * @Topic CustomExpressionDeParser
 * @Description 表达式解析器
 * @date 2021/7/27 15:06
 * @Version 1.0
 */
public class CustomExpressionDeParser extends ExpressionDeParser {

	private Tenant tenant;

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public CustomExpressionDeParser() {
	}

	public CustomExpressionDeParser(SelectVisitor selectVisitor, StringBuilder buffer) {
		super(selectVisitor, buffer);
	}

}

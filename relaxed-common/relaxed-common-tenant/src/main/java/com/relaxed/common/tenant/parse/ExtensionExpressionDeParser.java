package com.relaxed.common.tenant.parse;

import com.relaxed.common.tenant.core.Tenant;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;

/**
 * @author Yakir
 * @Topic CustomExpressionDeParser
 * @Description 表达式解析器
 * @date 2021/7/27 15:06
 * @Version 1.0
 */
public class ExtensionExpressionDeParser extends ExpressionDeParser {

	private Tenant tenant;

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public ExtensionExpressionDeParser() {
	}

	public ExtensionExpressionDeParser(SelectVisitor selectVisitor, StringBuilder buffer) {
		super(selectVisitor, buffer);
	}

}

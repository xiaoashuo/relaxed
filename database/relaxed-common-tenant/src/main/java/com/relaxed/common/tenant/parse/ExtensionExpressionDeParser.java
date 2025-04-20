package com.relaxed.common.tenant.parse;

import com.relaxed.common.tenant.core.Tenant;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;

/**
 * 扩展表达式解析器 继承自ExpressionDeParser，用于解析SQL表达式 支持租户信息的注入和处理
 *
 * @author Yakir
 */
public class ExtensionExpressionDeParser extends ExpressionDeParser {

	/**
	 * 租户信息
	 */
	private Tenant tenant;

	/**
	 * 获取租户信息
	 * @return 租户信息对象
	 */
	public Tenant getTenant() {
		return tenant;
	}

	/**
	 * 设置租户信息
	 * @param tenant 租户信息对象
	 */
	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	/**
	 * 默认构造函数
	 */
	public ExtensionExpressionDeParser() {
	}

	/**
	 * 带参数的构造函数
	 * @param selectVisitor 选择语句访问器
	 * @param buffer SQL语句构建器
	 */
	public ExtensionExpressionDeParser(SelectVisitor selectVisitor, StringBuilder buffer) {
		super(selectVisitor, buffer);
	}

}

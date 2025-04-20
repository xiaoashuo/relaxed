package com.relaxed.common.tenant.parse;

import com.relaxed.common.tenant.core.Tenant;
import com.relaxed.common.tenant.utils.ExpressionUtil;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.deparser.GroupByDeParser;
import net.sf.jsqlparser.util.deparser.LimitDeparser;
import net.sf.jsqlparser.util.deparser.OrderByDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;

import java.util.Iterator;

/**
 * 扩展的SQL查询访问器 继承自SelectDeParser，用于解析和处理SELECT语句 支持租户信息的注入和表名Schema的填充
 *
 * @author Yakir
 */
public class ExtensionSelectVisitor extends SelectDeParser {

	/**
	 * 租户信息对象
	 */
	private Tenant tenant;

	/**
	 * 构造函数
	 * @param expressionVisitor 表达式访问器
	 * @param buffer SQL语句构建器
	 */
	public ExtensionSelectVisitor(ExpressionVisitor expressionVisitor, StringBuilder buffer) {
		super(expressionVisitor, buffer);
	}

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
	 * 访问普通SELECT语句 处理SELECT语句的各个部分，包括： 1. 选择列 2. FROM子句 3. WHERE条件 4. GROUP BY 5. ORDER
	 * BY 6. LIMIT等
	 * @param plainSelect 普通SELECT语句对象
	 */
	@Override
	public void visit(PlainSelect plainSelect) {
		if (plainSelect.isUseBrackets()) {
			getBuffer().append("(");
		}

		getBuffer().append("SELECT ");
		if (plainSelect.getMySqlHintStraightJoin()) {
			getBuffer().append("STRAIGHT_JOIN ");
		}

		OracleHint hint = plainSelect.getOracleHint();
		if (hint != null) {
			getBuffer().append(hint).append(" ");
		}

		Skip skip = plainSelect.getSkip();
		if (skip != null) {
			getBuffer().append(skip).append(" ");
		}

		First first = plainSelect.getFirst();
		if (first != null) {
			getBuffer().append(first).append(" ");
		}

		if (plainSelect.getDistinct() != null) {
			if (plainSelect.getDistinct().isUseUnique()) {
				getBuffer().append("UNIQUE ");
			}
			else {
				getBuffer().append("DISTINCT ");
			}

			if (plainSelect.getDistinct().getOnSelectItems() != null) {
				getBuffer().append("ON (");
				Iterator iter = plainSelect.getDistinct().getOnSelectItems().iterator();

				while (iter.hasNext()) {
					SelectItem selectItem = (SelectItem) iter.next();
					selectItem.accept(this);
					if (iter.hasNext()) {
						getBuffer().append(", ");
					}
				}

				getBuffer().append(") ");
			}
		}

		Top top = plainSelect.getTop();
		if (top != null) {
			getBuffer().append(top).append(" ");
		}

		if (plainSelect.getMySqlSqlNoCache()) {
			getBuffer().append("SQL_NO_CACHE").append(" ");
		}

		if (plainSelect.getMySqlSqlCalcFoundRows()) {
			getBuffer().append("SQL_CALC_FOUND_ROWS").append(" ");
		}

		Iterator iter = plainSelect.getSelectItems().iterator();

		while (iter.hasNext()) {
			SelectItem selectItem = (SelectItem) iter.next();
			selectItem.accept(this);
			if (iter.hasNext()) {
				getBuffer().append(", ");
			}
		}

		if (plainSelect.getIntoTables() != null) {
			getBuffer().append(" INTO ");
			iter = plainSelect.getIntoTables().iterator();

			while (iter.hasNext()) {
				this.visit((Table) iter.next());
				if (iter.hasNext()) {
					getBuffer().append(", ");
				}
			}
		}

		if (plainSelect.getFromItem() != null) {
			getBuffer().append(" FROM ");
			plainSelect.getFromItem().accept(this);
		}

		if (plainSelect.getJoins() != null) {
			iter = plainSelect.getJoins().iterator();

			while (iter.hasNext()) {
				Join join = (Join) iter.next();
				this.deparseJoin(join);
			}
		}

		if (plainSelect.getKsqlWindow() != null) {
			getBuffer().append(" WINDOW ");
			getBuffer().append(plainSelect.getKsqlWindow().toString());
		}

		Expression whereExpression = plainSelect.getWhere();
		if (whereExpression != null) {
			getBuffer().append(" WHERE ");
			if (whereExpression != null) {
				Table fromItem = (Table) plainSelect.getFromItem();
				whereExpression = ExpressionUtil.injectExpression(whereExpression, fromItem, getTenant());
			}
			whereExpression.accept(super.getExpressionVisitor());
		}
		else {
			whereExpression = ExpressionUtil.injectExpressionNoWhere((Table) plainSelect.getFromItem(), getTenant(),
					getBuffer());
			if (whereExpression != null) {
				whereExpression.accept(super.getExpressionVisitor());
			}
		}

		if (plainSelect.getOracleHierarchical() != null) {
			plainSelect.getOracleHierarchical().accept(super.getExpressionVisitor());
		}

		if (plainSelect.getGroupBy() != null) {
			getBuffer().append(" ");
			(new GroupByDeParser(super.getExpressionVisitor(), getBuffer())).deParse(plainSelect.getGroupBy());
		}

		if (plainSelect.getHaving() != null) {
			getBuffer().append(" HAVING ");
			plainSelect.getHaving().accept(super.getExpressionVisitor());
		}

		if (plainSelect.getOrderByElements() != null) {
			(new OrderByDeParser(super.getExpressionVisitor(), getBuffer())).deParse(plainSelect.isOracleSiblings(),
					plainSelect.getOrderByElements());
		}

		if (plainSelect.getLimit() != null) {
			(new LimitDeparser(getBuffer())).deParse(plainSelect.getLimit());
		}

		if (plainSelect.getOffset() != null) {
			this.deparseOffset(plainSelect.getOffset());
		}

		if (plainSelect.getFetch() != null) {
			this.deparseFetch(plainSelect.getFetch());
		}

		if (plainSelect.isForUpdate()) {
			getBuffer().append(" FOR UPDATE");
			if (plainSelect.getForUpdateTable() != null) {
				getBuffer().append(" OF ").append(plainSelect.getForUpdateTable());
			}

			if (plainSelect.getWait() != null) {
				getBuffer().append(plainSelect.getWait());
			}

			if (plainSelect.isNoWait()) {
				getBuffer().append(" NOWAIT");
			}
		}

		if (plainSelect.getOptimizeFor() != null) {
			this.deparseOptimizeFor(plainSelect.getOptimizeFor());
		}

		if (plainSelect.getForXmlPath() != null) {
			getBuffer().append(" FOR XML PATH(").append(plainSelect.getForXmlPath()).append(")");
		}

		if (plainSelect.isUseBrackets()) {
			getBuffer().append(")");
		}

	}

	/**
	 * 解析优化FOR子句
	 * @param optimizeFor 优化FOR子句对象
	 */
	private void deparseOptimizeFor(OptimizeFor optimizeFor) {
		getBuffer().append(" OPTIMIZE FOR ");
		getBuffer().append(optimizeFor.getRowCount());
		getBuffer().append(" ROWS");
	}

	/**
	 * 访问表对象 填充表的Schema名称
	 * @param table 表对象
	 */
	@Override
	public void visit(Table table) {
		ExpressionUtil.fillTableSchema(table, getTenant());
		super.visit(table);
	}

}

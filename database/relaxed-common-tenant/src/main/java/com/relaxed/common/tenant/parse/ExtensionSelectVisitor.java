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
 * @author Yakir
 * @Topic CustomSelectVisitor
 * @Description 查询访问器
 * @date 2021/7/27 13:10
 * @Version 1.0
 */
public class ExtensionSelectVisitor extends SelectDeParser {

	private Tenant tenant;

	public ExtensionSelectVisitor(ExpressionVisitor expressionVisitor, StringBuilder buffer) {
		super(expressionVisitor, buffer);
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

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
	 * 解析优化
	 * @author yakir
	 * @date 2021/7/27 17:45
	 * @param optimizeFor
	 */
	private void deparseOptimizeFor(OptimizeFor optimizeFor) {
		getBuffer().append(" OPTIMIZE FOR ");
		getBuffer().append(optimizeFor.getRowCount());
		getBuffer().append(" ROWS");
	}

	@Override
	public void visit(Table table) {
		ExpressionUtil.fillTableSchema(table, getTenant());
		super.visit(table);

	}

}

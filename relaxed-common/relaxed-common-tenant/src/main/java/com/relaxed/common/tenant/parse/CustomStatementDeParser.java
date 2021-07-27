package com.relaxed.common.tenant.parse;

import com.relaxed.common.tenant.handler.Tenant;
import com.relaxed.common.tenant.utils.ExpressionUtil;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.deparser.*;

import java.util.Iterator;

/**
 * @author Yakir
 * @Topic CustomStatementDeParser
 * @Description 片段解析器
 * @date 2021/7/27 12:55
 * @Version 1.0
 */
public class CustomStatementDeParser extends StatementDeParser {

	private Tenant tenant;

	public CustomStatementDeParser(StringBuilder buffer, Tenant tenant) {
		super(buffer);
		this.tenant = tenant;
	}

	@Override
	public void visit(Insert insert) {
		ExpressionUtil.fillTableSchema(insert, getTenant());
		CustomExpressionDeParser expressionDeParser = new CustomExpressionDeParser();
		CustomSelectVisitor parser = new CustomSelectVisitor(expressionDeParser, getBuffer());
		parser.setTenant(getTenant());
		parser.setBuffer(getBuffer());
		expressionDeParser.setSelectVisitor(parser);
		expressionDeParser.setBuffer(getBuffer());

		InsertDeParser insertDeParser = new InsertDeParser(expressionDeParser, parser, getBuffer());
		insertDeParser.deParse(insert);
	}

	@Override
	public void visit(Update update) {
		ExpressionUtil.fillTableSchema(update, getTenant());
		CustomExpressionDeParser expressionDeParser = new CustomExpressionDeParser();
		CustomSelectVisitor parser = new CustomSelectVisitor(expressionDeParser, getBuffer());
		parser.setTenant(getTenant());
		parser.setBuffer(getBuffer());
		expressionDeParser.setSelectVisitor(parser);
		expressionDeParser.setBuffer(getBuffer());

		Expression whereExpression = update.getWhere();
		update.setWhere(processWhereExpression(whereExpression, update.getTable(), expressionDeParser, getBuffer(),
				getTenant()));
		UpdateDeParser updateDeParser = new UpdateDeParser(expressionDeParser, parser, getBuffer());
		updateDeParser.deParse(update);
	}

	@Override
	public void visit(Delete delete) {
		// 填充schema
		ExpressionUtil.fillTableSchema(delete, getTenant());
		CustomExpressionDeParser expressionDeParser = new CustomExpressionDeParser();
		CustomSelectVisitor parser = new CustomSelectVisitor(expressionDeParser, getBuffer());
		parser.setTenant(getTenant());
		parser.setBuffer(getBuffer());
		parser.setExpressionVisitor(expressionDeParser);
		expressionDeParser.setSelectVisitor(parser);
		expressionDeParser.setBuffer(getBuffer());
		Expression whereExpression = delete.getWhere();
		delete.setWhere(processWhereExpression(whereExpression, delete.getTable(), expressionDeParser, getBuffer(),
				getTenant()));
		DeleteDeParser deleteDeParser = new DeleteDeParser(expressionDeParser, getBuffer());
		deleteDeParser.deParse(delete);
	}

	@Override
	public void visit(Select select) {

		CustomExpressionDeParser expressionDeParser = new CustomExpressionDeParser();
		CustomSelectVisitor parser = new CustomSelectVisitor(expressionDeParser, getBuffer());
		parser.setTenant(getTenant());
		parser.setExpressionVisitor(expressionDeParser);
		expressionDeParser.setSelectVisitor(parser);
		expressionDeParser.setBuffer(getBuffer());
		if (select.getWithItemsList() != null && !select.getWithItemsList().isEmpty()) {
			getBuffer().append("WITH ");
			for (Iterator iter = select.getWithItemsList().iterator(); iter.hasNext(); getBuffer().append(" ")) {
				WithItem withItem = (WithItem) iter.next();
				withItem.accept(parser);
				if (iter.hasNext()) {
					getBuffer().append(",");
				}
			}
		}
		select.getSelectBody().accept(parser);

	}

	/**
	 * 处理where 表达式
	 * @author yakir
	 * @date 2021/7/27 20:06
	 * @param whereExpression
	 * @param table
	 * @param expressionDeParser
	 * @param builder
	 * @param tenant
	 * @return net.sf.jsqlparser.expression.Expression
	 */
	protected Expression processWhereExpression(Expression whereExpression, Table table,
			ExpressionDeParser expressionDeParser, StringBuilder builder, Tenant tenant) {

		if (whereExpression == null) {
			// 若开启字段匹配
			whereExpression = ExpressionUtil.processNoWhereExpression(table, tenant, builder);
			if (whereExpression != null) {
				whereExpression.accept(expressionDeParser);
			}
		}
		else {
			whereExpression = ExpressionUtil.injectExpression(whereExpression, table, getTenant());
		}
		return whereExpression;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

}

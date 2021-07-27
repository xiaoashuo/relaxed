package com.relaxed.common.tenant.parse;

import com.relaxed.common.tenant.handler.Tenant;
import com.relaxed.common.tenant.handler.table.DataScope;
import com.relaxed.common.tenant.holder.TenantHolder;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;
import net.sf.jsqlparser.util.deparser.StatementDeParser;
import net.sf.jsqlparser.util.deparser.UpdateDeParser;

import java.util.List;
import java.util.Objects;

/**
 * @author Yakir
 * @Topic DefaultSqlParser
 * @Description
 * @date 2021/7/26 19:54
 * @Version 1.0
 */
@Slf4j
public class DefaultSqlParser extends SqlParser {

	@Override
	public StatementDeParser getStatementDeParser(StringBuilder builder, Tenant tenant) {
		return new CustomStatementDeParser(builder, tenant);

	}

}

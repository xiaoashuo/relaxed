package com.relaxed.common.tenant.parse;

import com.relaxed.common.tenant.core.Tenant;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.util.deparser.StatementDeParser;

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
		return new ExtensionStatementDeParser(builder, tenant);

	}

}

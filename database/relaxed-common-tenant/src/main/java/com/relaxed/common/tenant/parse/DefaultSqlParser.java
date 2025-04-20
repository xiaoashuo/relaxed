package com.relaxed.common.tenant.parse;

import com.relaxed.common.tenant.core.Tenant;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.util.deparser.StatementDeParser;

/**
 * 默认SQL解析器实现 继承自SqlParser，提供基本的SQL解析功能 使用ExtensionStatementDeParser进行SQL语句的解析和修改
 *
 * @author Yakir
 */
@Slf4j
public class DefaultSqlParser extends SqlParser {

	/**
	 * 获取SQL语句解析器 创建并返回ExtensionStatementDeParser实例，用于处理SQL语句
	 * @param builder SQL语句构建器
	 * @param tenant 租户信息
	 * @return SQL语句解析器
	 */
	@Override
	public StatementDeParser getStatementDeParser(StringBuilder builder, Tenant tenant) {
		return new ExtensionStatementDeParser(builder, tenant);
	}

}

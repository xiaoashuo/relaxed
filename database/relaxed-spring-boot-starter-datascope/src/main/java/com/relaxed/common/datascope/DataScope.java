package com.relaxed.common.datascope;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;

import java.util.Collection;

/**
 * 数据权限范围接口
 * <p>
 * 定义数据权限范围的核心功能，包括资源标识、表名集合和权限过滤条件。 实现类需要提供具体的权限控制逻辑。
 */
public interface DataScope {

	/**
	 * 获取数据权限对应的资源标识
	 * <p>
	 * 用于标识当前数据权限规则所控制的资源类型。
	 * @return 资源标识
	 */
	String getResource();

	/**
	 * 获取受权限控制的表名集合
	 * <p>
	 * 返回需要应用数据权限规则的所有表名。 建议使用 Set 类型，如需忽略大小写可使用 TreeSet 并设置
	 * String.CASE_INSENSITIVE_ORDER。
	 * @return 表名集合
	 */
	Collection<String> getTableNames();

	/**
	 * 生成数据权限过滤条件
	 * <p>
	 * 根据表名和别名生成对应的 where 或 or 条件表达式。
	 * @param tableName 表名
	 * @param tableAlias 表别名，可能为空
	 * @return 数据权限过滤条件表达式
	 */
	Expression getExpression(String tableName, Alias tableAlias);

}

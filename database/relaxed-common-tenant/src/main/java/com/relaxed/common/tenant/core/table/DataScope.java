package com.relaxed.common.tenant.core.table;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;

import java.util.Collection;

/**
 * 数据域接口 用于定义表级别的租户数据隔离规则 包含资源标识、相关表集合以及数据过滤表达式的生成规则
 *
 * @author Yakir
 */
public interface DataScope {

	/**
	 * 获取数据域的资源标识 用于唯一标识一个数据域，通常与业务模块对应
	 * @return String 资源标识符
	 */
	String getResource();

	/**
	 * 获取数据域关联的所有表名集合 推荐使用Set类型来存储表名，避免重复 如果需要忽略表名大小写，可以使用TreeSet并配置忽略大小写的比较器
	 * @return 表名集合
	 */
	Collection<String> getTableNames();

	/**
	 * 生成数据过滤表达式 根据表名和表别名动态生成WHERE/OR条件，用于SQL数据过滤
	 * @param tableName 需要进行数据过滤的表名
	 * @param tableAlias 表的别名，在SQL中可能存在的表别名，可能为空
	 * @return Expression SQL表达式，用于数据过滤
	 */
	Expression getExpression(String tableName, Alias tableAlias);

}

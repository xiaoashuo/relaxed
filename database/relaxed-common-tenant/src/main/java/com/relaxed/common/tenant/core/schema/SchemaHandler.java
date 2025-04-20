package com.relaxed.common.tenant.core.schema;

import java.util.List;

/**
 * Schema级别的多租户处理器接口 用于处理数据库Schema级别的多租户隔离策略 提供Schema级别的租户配置、忽略规则和当前Schema获取等功能
 *
 * @author Yakir
 */
public interface SchemaHandler {

	/**
	 * 判断是否启用Schema级别的多租户功能 用于控制是否对SQL进行Schema级别的租户隔离处理
	 * @return boolean true: 启用多租户, false: 不启用多租户
	 */
	boolean enable();

	/**
	 * 判断是否忽略指定的Schema 用于设置某些特定Schema不需要进行租户隔离处理
	 * @param schemaName Schema名称
	 * @return boolean true: 忽略该Schema, false: 不忽略该Schema
	 */
	boolean ignore(String schemaName);

	/**
	 * 判断是否忽略指定的Mapper方法 用于设置某些特定的SQL操作不需要进行租户隔离处理
	 * @param mapperId Mapper方法的全限定名
	 * @return boolean true: 忽略该方法, false: 不忽略该方法
	 */
	boolean ignoreMethod(String mapperId);

	/**
	 * 获取当前租户的Schema名称 如果使用指定的Schema，则返回对应的Schema名称 如果使用当前数据库的默认Schema，则返回null
	 * @return String Schema名称，可能为null
	 */
	String getCurrentSchema();

}

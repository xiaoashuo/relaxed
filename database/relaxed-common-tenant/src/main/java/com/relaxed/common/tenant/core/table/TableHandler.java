package com.relaxed.common.tenant.core.table;

import java.util.List;

/**
 * 表级别租户处理器接口 用于处理数据表级别的多租户隔离策略 提供租户字段的配置、数据范围的过滤和忽略规则等功能
 *
 * @author Yakir
 */
public interface TableHandler {

	/**
	 * 判断是否启用表级别的多租户功能 用于控制是否对SQL进行表级别的租户字段隔离处理
	 * @return boolean true: 启用多租户, false: 不启用多租户
	 */
	boolean enable();

	/**
	 * 获取系统配置的所有数据范围 返回系统中定义的所有数据域配置，用于多租户数据隔离
	 * @return 所有配置的数据范围列表
	 */
	List<DataScope> dataScopes();

	/**
	 * 获取经过权限过滤后的数据范围集合 根据Mapper方法的权限注解，过滤出当前方法可以访问的数据范围
	 * @param mappedStatementId Mapper方法的全限定名
	 * @return 过滤后的数据范围列表
	 */
	List<DataScope> filterDataScopes(String mappedStatementId);

	/**
	 * 判断是否忽略数据权限处理 用于设置某些特定的SQL操作不需要进行租户数据隔离处理
	 * @param mappedStatementId Mapper方法的全限定名
	 * @return boolean true: 忽略该方法的数据权限, false: 处理该方法的数据权限
	 */
	boolean ignore(String mappedStatementId);

}

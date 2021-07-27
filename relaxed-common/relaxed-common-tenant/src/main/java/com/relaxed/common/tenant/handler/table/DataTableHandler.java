package com.relaxed.common.tenant.handler.table;

import java.util.List;

/**
 * @author Yakir
 * @Topic DataTableManage
 * @Description 针对数据表 的指定租户字段
 * @date 2021/7/27 9:54
 * @Version 1.0
 */
public interface DataTableHandler {

	/**
	 * 是否开启字段多租户
	 * @author yakir
	 * @date 2021/7/27 18:02
	 * @return boolean
	 */
	boolean enable();

	/**
	 * 系统配置的所有的数据范围
	 * @return 数据范围集合
	 */
	List<DataScope> dataScopes();

	/**
	 * 根据权限注解过滤后的数据范围集合
	 * @param mappedStatementId Mapper方法ID
	 * @return 数据范围集合
	 */
	List<DataScope> filterDataScopes(String mappedStatementId);

	/**
	 * 忽略数据权限处理
	 * @param mapperId mapper 全路径
	 * @return true 忽略 false 处理数据权限
	 */
	boolean ignore(String mapperId);

}

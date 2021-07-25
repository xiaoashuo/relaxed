package com.relaxed.common.datascope.handler;

import com.relaxed.common.datascope.DataScope;

import java.util.List;

/**
 * @author Yakir
 * @Topic DataPermissionHandler
 * @Description
 * @date 2021/7/25 14:19
 * @Version 1.0
 */
public interface DataPermissionHandler {

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

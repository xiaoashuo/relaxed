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
	 * 是否忽略权限控制，用于及早的忽略控制，例如管理员直接放行，而不必等到DataScope中再进行过滤处理，提升效率
	 * @return boolean true: 忽略，false: 进行权限控制
	 * @param dataScopeList 当前应用的 dataScope 集合
	 * @param mappedStatementId Mapper方法ID
	 */
	boolean ignorePermissionControl(List<DataScope> dataScopeList, String mappedStatementId);

	/**
	 * 使用指定的数据权限执行任务，执行时会忽略方法上的 @DataPermission 注解
	 * @param dataPermissionRule 当前任务执行时使用的数据权限规则
	 * @param task 待执行的任务
	 */
	void executeWithDataPermissionRule(DataPermissionRule dataPermissionRule, Task task);

	/**
	 * 任务接口
	 */
	@FunctionalInterface
	interface Task {

		/**
		 * 执行任务
		 */
		void perform();

	}

}

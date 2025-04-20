package com.relaxed.common.datascope.handler;

import com.relaxed.common.datascope.DataScope;

import java.util.List;

/**
 * 数据权限处理器接口
 * <p>
 * 定义数据权限处理的核心功能，包括获取数据范围、过滤数据范围、权限控制判断等。 实现此接口可以自定义数据权限的处理逻辑。
 */
public interface DataPermissionHandler {

	/**
	 * 获取系统配置的所有数据范围
	 * <p>
	 * 返回系统中定义的所有数据权限范围。
	 * @return 数据范围集合
	 */
	List<DataScope> dataScopes();

	/**
	 * 根据权限注解过滤数据范围
	 * <p>
	 * 根据 Mapper 方法上的数据权限注解，过滤出需要应用的数据范围。
	 * @param mappedStatementId Mapper 方法 ID
	 * @return 过滤后的数据范围集合
	 */
	List<DataScope> filterDataScopes(String mappedStatementId);

	/**
	 * 判断是否忽略权限控制
	 * <p>
	 * 用于提前判断是否需要忽略权限控制，例如管理员可以直接放行， 避免在 DataScope 中进行不必要的过滤处理，提升效率。
	 * @param dataScopeList 当前应用的数据范围集合
	 * @param mappedStatementId Mapper 方法 ID
	 * @return 是否忽略权限控制，true 表示忽略，false 表示进行权限控制
	 */
	boolean ignorePermissionControl(List<DataScope> dataScopeList, String mappedStatementId);

	/**
	 * 使用指定的数据权限规则执行任务
	 * <p>
	 * 执行任务时会忽略方法上的 @DataPermission 注解， 直接使用指定的数据权限规则。
	 * @param dataPermissionRule 当前任务执行时使用的数据权限规则
	 * @param task 待执行的任务
	 */
	void executeWithDataPermissionRule(DataPermissionRule dataPermissionRule, Task task);

	/**
	 * 任务接口
	 * <p>
	 * 定义可执行的任务，用于在指定数据权限规则下执行特定操作。
	 */
	@FunctionalInterface
	interface Task {

		/**
		 * 执行任务
		 * <p>
		 * 实现此方法定义具体的任务执行逻辑。
		 */
		void perform();

	}

}

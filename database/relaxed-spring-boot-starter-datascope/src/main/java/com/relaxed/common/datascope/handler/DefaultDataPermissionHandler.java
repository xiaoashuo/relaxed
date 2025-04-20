package com.relaxed.common.datascope.handler;

import com.relaxed.common.datascope.DataScope;
import com.relaxed.common.datascope.holder.DataPermissionRuleHolder;
import com.relaxed.common.datascope.holder.MappedStatementIdsWithoutDataScope;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 默认的数据权限控制处理器
 * <p>
 * 实现了 DataPermissionHandler 接口，提供了默认的数据权限控制逻辑。 支持根据权限规则过滤数据范围，以及忽略特定 Mapper 方法的权限控制。
 */
@RequiredArgsConstructor
public class DefaultDataPermissionHandler implements DataPermissionHandler {

	private final List<DataScope> dataScopes;

	/**
	 * 获取系统配置的所有数据范围
	 * <p>
	 * 返回构造函数中注入的数据范围集合。
	 * @return 数据范围集合
	 */
	@Override
	public List<DataScope> dataScopes() {
		return dataScopes;
	}

	/**
	 * 根据权限规则过滤数据范围
	 * <p>
	 * 根据当前线程中的权限规则，过滤出需要应用的数据范围。 如果数据范围集合为空，则返回空列表。
	 * @param mappedStatementId Mapper 方法 ID
	 * @return 过滤后的数据范围集合
	 */
	@Override
	public List<DataScope> filterDataScopes(String mappedStatementId) {
		if (this.dataScopes == null || this.dataScopes.isEmpty()) {
			return new ArrayList<>();
		}
		// 获取权限规则
		DataPermissionRule dataPermissionRule = DataPermissionRuleHolder.peek();
		return filterDataScopes(dataPermissionRule);
	}

	/**
	 * 判断是否忽略权限控制
	 * <p>
	 * 如果当前 Mapper 方法 ID 存在于 MappedStatementIdsWithoutDataScope 中， 则表示该方法不需要进行数据权限控制。
	 * @param dataScopeList 当前需要控制的数据范围集合
	 * @param mappedStatementId Mapper 方法 ID
	 * @return 是否忽略权限控制
	 */
	@Override
	public boolean ignorePermissionControl(List<DataScope> dataScopeList, String mappedStatementId) {
		return MappedStatementIdsWithoutDataScope.onAllWithoutSet(dataScopeList, mappedStatementId);
	}

	/**
	 * 使用指定的数据权限规则执行任务
	 * <p>
	 * 将指定的权限规则压入线程上下文，执行任务后再弹出。 确保任务在指定的权限规则下执行。
	 * @param dataPermissionRule 当前任务执行时使用的数据权限规则
	 * @param task 待执行的任务
	 */
	@Override
	public void executeWithDataPermissionRule(DataPermissionRule dataPermissionRule, Task task) {
		DataPermissionRuleHolder.push(dataPermissionRule);
		try {
			task.perform();
		}
		finally {
			DataPermissionRuleHolder.poll();
		}
	}

	/**
	 * 根据数据权限规则过滤数据范围
	 * <p>
	 * 根据权限规则的配置（忽略、包含资源、排除资源）过滤数据范围。 如果规则为空，则返回所有数据范围。
	 * @param dataPermissionRule 数据权限规则
	 * @return 过滤后的数据范围列表
	 */
	protected List<DataScope> filterDataScopes(DataPermissionRule dataPermissionRule) {
		if (dataPermissionRule == null) {
			return dataScopes;
		}

		if (dataPermissionRule.ignore()) {
			return new ArrayList<>();
		}

		// 当指定了只包含的资源时，只对该资源的DataScope
		if (dataPermissionRule.includeResources().length > 0) {
			Set<String> a = new HashSet<>(Arrays.asList(dataPermissionRule.includeResources()));
			return dataScopes.stream().filter(x -> a.contains(x.getResource())).collect(Collectors.toList());
		}

		// 当未指定只包含的资源，且指定了排除的资源时，则排除此部分资源的 DataScope
		if (dataPermissionRule.excludeResources().length > 0) {
			Set<String> a = new HashSet<>(Arrays.asList(dataPermissionRule.excludeResources()));
			return dataScopes.stream().filter(x -> !a.contains(x.getResource())).collect(Collectors.toList());
		}

		return dataScopes;
	}

}

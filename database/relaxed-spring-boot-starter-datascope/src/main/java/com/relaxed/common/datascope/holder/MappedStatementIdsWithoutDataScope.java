package com.relaxed.common.datascope.holder;

import com.relaxed.common.datascope.DataScope;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据权限忽略的 Mapper 方法 ID 持有者
 * <p>
 * 用于存储不需要进行数据权限处理的 Mapper 方法 ID 集合。 每个 DataScope 类型对应一个忽略列表，用于快速判断某个方法是否需要数据权限控制。
 */
public final class MappedStatementIdsWithoutDataScope {

	private MappedStatementIdsWithoutDataScope() {
	}

	/**
	 * 存储不需要数据权限处理的 Mapper 方法 ID
	 * <p>
	 * key: DataScope 类型 value: 该 DataScope 不需要处理的 Mapper 方法 ID 集合
	 */
	private static final Map<Class<? extends DataScope>, HashSet<String>> WITHOUT_MAPPED_STATEMENT_ID_MAP = new ConcurrentHashMap<>();

	/**
	 * 将 Mapper 方法 ID 添加到所有 DataScope 的忽略列表中
	 * <p>
	 * 将指定的 Mapper 方法 ID 添加到所有 DataScope 对应的忽略列表中， 表示该方法不需要进行数据权限控制。
	 * @param dataScopeList 数据范围集合
	 * @param mappedStatementId Mapper 方法 ID
	 */
	public static void addToWithoutSet(List<DataScope> dataScopeList, String mappedStatementId) {
		for (DataScope dataScope : dataScopeList) {
			Class<? extends DataScope> dataScopeClass = dataScope.getClass();
			HashSet<String> set = WITHOUT_MAPPED_STATEMENT_ID_MAP.computeIfAbsent(dataScopeClass,
					key -> new HashSet<>());
			set.add(mappedStatementId);
		}
	}

	/**
	 * 判断是否可以忽略数据权限控制
	 * <p>
	 * 检查当前 Mapper 方法 ID 是否存在于所有需要控制的 DataScope 对应的忽略列表中。 只有当所有 DataScope 都忽略该 Mapper
	 * 方法时，才返回 true。
	 * @param dataScopeList 数据范围集合
	 * @param mappedStatementId Mapper 方法 ID
	 * @return 是否可以忽略数据权限控制
	 */
	public static boolean onAllWithoutSet(List<DataScope> dataScopeList, String mappedStatementId) {
		for (DataScope dataScope : dataScopeList) {
			Class<? extends DataScope> dataScopeClass = dataScope.getClass();
			HashSet<String> set = WITHOUT_MAPPED_STATEMENT_ID_MAP.computeIfAbsent(dataScopeClass,
					key -> new HashSet<>());
			if (!set.contains(mappedStatementId)) {
				return false;
			}
		}
		return true;
	}

}

package com.relaxed.common.core.constants;

/**
 * 全局常量表，定义了系统中使用的全局常量值。 包括环境标识、逻辑删除标识、树形结构根节点ID等。
 *
 * @author Yakir
 * @since 1.0
 */
public class GlobalConstants {

	private GlobalConstants() {

	}

	/**
	 * 生产环境标识
	 */
	public static final String ENV_PROD = "prod";

	/**
	 * 未被逻辑删除的标识，即有效数据标识。 在逻辑删除场景下：
	 * <ul>
	 * <li>0 表示数据有效</li>
	 * <li>非0值（通常是时间戳）表示数据已被删除</li>
	 * </ul>
	 * 使用时间戳作为删除标识可以避免唯一索引冲突问题。
	 */
	public static final Long NOT_DELETED_FLAG = 0L;

	/**
	 * 树形结构的根节点ID
	 */
	public static final Integer TREE_ROOT_ID = 0;

}

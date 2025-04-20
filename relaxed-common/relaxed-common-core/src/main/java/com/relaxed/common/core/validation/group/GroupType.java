package com.relaxed.common.core.validation.group;

/**
 * 验证分组类型接口，定义了常用的验证分组。 该接口用于在参数验证时指定不同的验证规则组，支持按业务场景进行分组验证。
 *
 * @author Yakir
 * @since 1.0
 */
public interface GroupType {

	/**
	 * 新增数据时的验证分组
	 */
	interface CreateGroup {

	}

	/**
	 * 修改数据时的验证分组
	 */
	interface UpdateGroup {

	}

}

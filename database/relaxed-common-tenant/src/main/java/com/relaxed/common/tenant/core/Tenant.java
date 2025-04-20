package com.relaxed.common.tenant.core;

import com.relaxed.common.tenant.core.table.DataScope;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 租户信息封装类 用于封装多租户场景下的租户信息，包括Schema级别和表级别的租户隔离配置 支持链式调用，方便设置租户属性
 *
 * @author Yakir
 * @since 1.0
 */
@Accessors(chain = true)
@Data
public class Tenant {

	/**
	 * Schema级别租户隔离开关 true: 启用Schema级别租户隔离 false: 不启用Schema级别租户隔离
	 */
	private boolean schema;

	/**
	 * Schema名称 用于指定当前租户使用的数据库Schema 格式为反引号包裹的Schema名称，如：`tenant1`
	 */
	private String schemaName;

	/**
	 * 表级别租户隔离开关 true: 启用表级别租户隔离 false: 不启用表级别租户隔离
	 */
	private boolean table;

	/**
	 * 数据域列表 用于指定表级别租户隔离的数据范围 包含多个数据域，每个数据域定义了一组表级别的租户隔离规则
	 */
	private List<DataScope> dataScopes;

	/**
	 * 设置Schema名称 自动为Schema名称添加反引号，确保SQL语句的正确性
	 * @param schemaName 原始Schema名称
	 */
	public void setSchemaName(String schemaName) {
		this.schemaName = String.format("`%s`", schemaName);
	}

}

package com.relaxed.common.tenant.handler;

import com.relaxed.common.tenant.handler.table.DataScope;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Yakir
 * @Topic Tenant
 * @Description 租户透传参数处理
 * @date 2021/7/27 10:06
 * @Version 1.0
 */
@Accessors(chain = true)
@Data
public class Tenant {

	/**
	 * 是否处理schema true 处理 false 不处理
	 */
	private boolean schema = false;

	/**
	 * schema name 名称 对应schema 控制
	 */
	private String schemaName;

	/**
	 * 是否处理字段 true 处理 false 不处理
	 */
	private boolean table = false;

	/**
	 * 数据列表 对应table 控制
	 */
	private List<DataScope> dataScopes;

	public void setSchemaName(String schemaName) {
		this.schemaName = String.format("`%s`", schemaName);
	}

}

package com.relaxed.common.tenant.manage;

import java.util.List;

/**
 * @author Yakir
 * @Topic DataSourceMannage
 * @Description
 * @date 2021/7/26 16:23
 * @Version 1.0
 */
public interface DataSchemaManage {

	/**
	 * 获取当前系统所有的数据库标识
	 * @author yakir
	 * @date 2021/7/26 16:47
	 * @return java.util.List<java.lang.String>
	 */
	List<String> getAllSchemas();

	/**
	 * 得到所有忽略的schemas 不需要多租户处理的
	 * @author yakir
	 * @date 2021/7/26 16:58
	 * @return java.util.List<java.lang.String>
	 */
	List<String> getIgnoreSchemas();

	/**
	 * 是否忽略当前schema
	 * @author yakir
	 * @date 2021/7/26 16:52
	 * @param schemaName
	 * @return boolean true 忽略 false 不忽略
	 */
	boolean ignore(String schemaName);

	/**
	 * 忽略指定方法
	 * @author yakir
	 * @date 2021/7/26 17:17
	 * @param mapperId 全路径名称
	 * @return boolean
	 */
	boolean ignoreMethod(String mapperId);

	/**
	 * 验证当前schema 是否在所有schema 内
	 * @author yakir
	 * @date 2021/7/26 17:08
	 * @param schemaName
	 * @return boolean
	 */
	boolean validSchema(String schemaName);

}

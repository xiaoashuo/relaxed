package com.relaxed.common.tenant.core.schema;

import java.util.List;

/**
 * @author Yakir
 * @Topic SchemaHandler
 * @Description 针对数据库的
 * @date 2021/7/26 16:23
 * @Version 1.0
 */
public interface SchemaHandler {

	/**
	 * 是否开启schema 多租户
	 * @author yakir
	 * @date 2021/7/27 18:01
	 * @return boolean
	 */
	boolean enable();

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
	 * 提供数据库Schema 若指定schema 则返回对应名称 若使用当前数据库的 则返回null
	 * @return
	 */
	String getCurrentSchema();

}

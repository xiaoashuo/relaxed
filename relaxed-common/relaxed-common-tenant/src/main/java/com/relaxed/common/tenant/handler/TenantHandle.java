package com.relaxed.common.tenant.handler;

import java.util.List;

/**
 * @author Yakir
 * @Topic TenantHandle
 * @Description
 * @date 2021/7/26 14:47
 * @Version 1.0
 */
public interface TenantHandle {

	/**
	 * 是否启用多租户 全局统一控制
	 * @author yakir
	 * @date 2021/7/26 17:34
	 * @return boolean
	 */
	boolean enable();

	/**
	 * 提供数据库Schema 若指定schema 则返回对应名称 若使用当前数据库的 则返回null
	 * @return
	 */
	String getCurrentSchema();

}

package com.relaxed.common.security.jwt.tookit;

/**
 * @author Yakir
 * @Topic IPermissionEvaluator
 * @Description
 * @date 2021/8/18 15:03
 * @Version 1.0
 */
public interface IPermissionEvaluator {

	/**
	 * 判断是否有权限
	 * @author yakir
	 * @date 2021/8/18 15:03
	 * @param permission
	 * @return boolean
	 */
	boolean hasPermission(String permission);

}

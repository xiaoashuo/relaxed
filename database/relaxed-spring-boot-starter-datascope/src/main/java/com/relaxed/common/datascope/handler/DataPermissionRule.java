package com.relaxed.common.datascope.handler;

import com.relaxed.common.datascope.annotation.DataPermission;

/**
 * 数据权限规则类
 * <p>
 * 用于封装数据权限的配置规则，包括是否忽略权限控制、包含和排除的资源类型等。 可以通过 DataPermission 注解或直接设置属性来配置规则。
 */
public class DataPermissionRule {

	private boolean ignore = false;

	private String[] includeResources = new String[0];

	private String[] excludeResources = new String[0];

	public DataPermissionRule() {
	}

	public DataPermissionRule(DataPermission dataPermission) {
		this.ignore = dataPermission.ignore();
		this.includeResources = dataPermission.includeResources();
		this.excludeResources = dataPermission.excludeResources();
	}

	/**
	 * 判断是否忽略数据权限控制
	 * <p>
	 * 返回当前规则是否忽略数据权限控制。
	 * @return 是否忽略数据权限，默认为 false
	 */
	public boolean ignore() {
		return ignore;
	}

	/**
	 * 获取需要控制数据权限的资源类型
	 * <p>
	 * 返回需要进行数据权限控制的资源类型数组。 如果设置了该属性，则 excludeResources 属性将失效。
	 * @return 资源类型数组
	 */
	public String[] includeResources() {
		return includeResources;
	}

	/**
	 * 获取需要跳过数据权限控制的资源类型
	 * <p>
	 * 返回需要跳过数据权限控制的资源类型数组。 如果 includeResources 属性不为空，则该属性将失效。
	 * @return 资源类型数组
	 */
	public String[] excludeResources() {
		return excludeResources;
	}

	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}

	public void setIncludeResources(String[] includeResources) {
		this.includeResources = includeResources;
	}

	public void setExcludeResources(String[] excludeResources) {
		this.excludeResources = excludeResources;
	}

}

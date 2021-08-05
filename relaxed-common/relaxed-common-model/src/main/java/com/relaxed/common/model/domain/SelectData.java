package com.relaxed.common.model.domain;

import lombok.Data;

/**
 * 下拉框选择数据
 *
 * @author Yakir
 * @since 2021/3/20
 */
@Data
public class SelectData<T> {

	/**
	 * 显示的数据
	 */
	private String name;

	/**
	 * 选中获取的属性
	 */
	private String value;

	/**
	 * 是否被选中
	 */
	private String selected;

	/**
	 * 是否禁用
	 */
	private String disabled;

	/**
	 * 分组标识
	 */
	private String type;

	/**
	 * 扩展对象
	 */
	private T extendObj;

}

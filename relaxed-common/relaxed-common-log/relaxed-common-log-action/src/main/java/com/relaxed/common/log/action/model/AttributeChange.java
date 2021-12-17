package com.relaxed.common.log.action.model;

import lombok.Data;

/**
 * @author Yakir
 * @Topic AttributeChange
 * @Description
 * @date 2021/12/17 10:28
 * @Version 1.0
 */
@Data
public class AttributeChange {

	/**
	 * 操作
	 */
	private String op;

	/**
	 * 属性
	 */
	private String property;

	/**
	 * 路径
	 */
	private String path;

	/**
	 * 源对象值
	 */
	private String leftValue;

	/**
	 * 目标对象值
	 */
	private String rightValue;

}

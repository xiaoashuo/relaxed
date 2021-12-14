package com.relaxed.common.log.action.model;

import lombok.Data;

import java.util.List;

/**
 * @author Yakir
 * @Topic AttributeModel
 * @Description 属性model
 * @date 2021/12/14 13:56
 * @Version 1.0
 */
@Data
public class AttributeModel {

	/**
	 * 属性类型 用来区别差异值 的处理方式
	 */
	private String attributeType;

	/**
	 * 属性名称
	 */
	private String attributeName;

	/**
	 * 属性别名
	 */
	private String attributeAlias;

	/**
	 * 旧值
	 */
	private String oldValue;

	/**
	 * 新值
	 */
	private String newValue;

	/**
	 * 差异值
	 */
	private String diffValue;

}

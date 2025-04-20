package com.relaxed.common.log.biz.model;

import lombok.Data;

/**
 * 属性模型类，用于描述对象属性的变更信息 该类用于记录对象属性的类型、名称、别名以及新旧值的变化 支持记录属性的差异值，用于展示属性变更的具体内容
 *
 * @author Yakir
 */
@Data
public class AttributeModel {

	/**
	 * 属性类型，用于区分不同属性的处理方式 例如：文本、数字、日期等
	 */
	private String attributeType;

	/**
	 * 属性名称，记录属性的原始名称
	 */
	private String attributeName;

	/**
	 * 属性别名，用于展示时的友好名称 例如：将 "createTime" 显示为 "创建时间"
	 */
	private String attributeAlias;

	/**
	 * 旧值，记录属性变更前的值
	 */
	private String oldValue;

	/**
	 * 新值，记录属性变更后的值
	 */
	private String newValue;

	/**
	 * 差异值，记录属性变更的具体差异 用于展示属性变更的详细信息
	 */
	private String diffValue;

}

package com.relaxed.common.log.biz.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 属性变更类，用于记录对象属性在操作前后的变化 该类用于比较两个对象之间的差异，记录属性的具体变更情况 支持记录属性的操作类型、路径、新旧值等信息
 *
 * @author Yakir
 */
@Data
public class AttributeChange {

	/**
	 * 操作类型，描述属性变更的具体操作 例如：新增、删除、修改等
	 */
	private String op;

	/**
	 * 属性名称，记录发生变更的属性名
	 */
	private String property;

	/**
	 * 属性路径，记录属性在对象中的完整路径 例如：user.address.city
	 */
	private String path;

	/**
	 * 源对象值，记录变更前的属性值
	 */
	private String leftValue;

	/**
	 * 目标对象值，记录变更后的属性值
	 */
	private String rightValue;

	/**
	 * 扩展参数，用于存储额外的属性变更信息 支持自定义扩展，可以根据需要添加额外的参数
	 */
	private Map<String, String> extParam = new HashMap<>();

}

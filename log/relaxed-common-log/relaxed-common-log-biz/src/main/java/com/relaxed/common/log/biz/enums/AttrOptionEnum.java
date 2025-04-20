package com.relaxed.common.log.biz.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

/**
 * 属性操作类型枚举，用于标识对象属性在变更过程中的操作类型。 主要用于记录对象属性在修改前后的变化情况，支持以下操作类型： 1. 无变动：属性值未发生变化 2.
 * 添加：新增了属性值 3. 删除：移除了属性值 4. 替换：属性值被新的值替换
 *
 * @author Yakir
 * @since 1.0.0
 */
@Getter
public enum AttrOptionEnum {

	/**
	 * 无变动，表示属性值在修改前后保持一致
	 */
	NONE,

	/**
	 * 添加，表示属性值从无到有，即新增了该属性
	 */
	ADD,

	/**
	 * 删除，表示属性值从有到无，即移除了该属性
	 */
	REMOVE,

	/**
	 * 替换，表示属性值被新的值替换
	 */
	REPLACE;

	/**
	 * 根据属性值的变化情况判断操作类型 判断逻辑： 1. 如果旧值为空且新值不为空，则为添加操作 2. 如果旧值不为空且新值为空，则为删除操作 3.
	 * 如果旧值和新值都不为空且不相等，则为替换操作 4. 其他情况为无变动
	 * @param oldFieldValue 修改前的属性值
	 * @param newFieldValue 修改后的属性值
	 * @return 对应的操作类型枚举值
	 */
	public static AttrOptionEnum changeTypeEnum(Object oldFieldValue, Object newFieldValue) {
		AttrOptionEnum op;

		if (ObjectUtil.isEmpty(oldFieldValue) && ObjectUtil.isNotEmpty(newFieldValue)) {
			op = AttrOptionEnum.ADD;
		}
		else if (ObjectUtil.isNotEmpty(oldFieldValue) && ObjectUtil.isEmpty(newFieldValue)) {
			op = AttrOptionEnum.REMOVE;
		}
		else if (ObjectUtil.notEqual(oldFieldValue, newFieldValue)) {
			op = AttrOptionEnum.REPLACE;
		}
		else {
			op = AttrOptionEnum.NONE;
		}
		return op;
	}

	/**
	 * 根据操作类型字符串获取对应的枚举值 支持的字符串值： - "add": 添加操作 - "remove": 删除操作 - "replace": 替换操作
	 * @param op 操作类型字符串
	 * @return 对应的操作类型枚举值
	 * @throws RuntimeException 如果传入的操作类型字符串不合法
	 */
	public static AttrOptionEnum of(String op) {
		AttrOptionEnum attrOptionEnum;
		switch (op) {
		case "add":
			attrOptionEnum = ADD;
			break;
		case "remove":
			attrOptionEnum = REMOVE;
			break;
		case "replace":
			attrOptionEnum = REPLACE;
			break;
		default:
			throw new RuntimeException("op not exist" + op);
		}
		return attrOptionEnum;
	}

}

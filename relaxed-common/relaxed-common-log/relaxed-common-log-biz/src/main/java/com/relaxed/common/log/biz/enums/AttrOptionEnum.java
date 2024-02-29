package com.relaxed.common.log.biz.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

/**
 * @author Yakir
 * @Topic AttrOptionEnum
 * @Description
 * @date 2021/12/17 10:30
 * @Version 1.0
 */
@Getter
public enum AttrOptionEnum {

	/**
	 * 无变动
	 */
	NONE,
	/**
	 * 添加
	 */
	ADD,

	/**
	 * 删除
	 */
	REMOVE,
	/**
	 * 替换
	 */
	REPLACE,

	;

	/**
	 * 枚举类型获取
	 * @param oldFieldValue
	 * @param newFieldValue
	 * @return
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

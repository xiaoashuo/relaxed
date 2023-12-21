package com.relaxed.common.log.biz.enums;

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

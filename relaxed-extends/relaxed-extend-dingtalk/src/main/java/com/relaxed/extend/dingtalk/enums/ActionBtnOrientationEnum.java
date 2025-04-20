package com.relaxed.extend.dingtalk.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * ActionBtnOrientationEnum
 * @author Yakir
 */
@Getter
@RequiredArgsConstructor
public enum ActionBtnOrientationEnum {

	/**
	 * 竖向
	 */
	VERTICAL("0", "按钮竖向排列"),

	/**
	 * 横向
	 */
	HORIZONTAL("1", "按钮横向排列"),;

	private final String orientation;

	private final String text;

}

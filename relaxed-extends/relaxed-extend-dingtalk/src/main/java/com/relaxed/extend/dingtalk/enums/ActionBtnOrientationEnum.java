package com.relaxed.extend.dingtalk.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Yakir
 * @Topic ActionBtnOrientationEnum
 * @Description 按钮排列样式值
 * @date 2021/6/19
 * @Version 1.0
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

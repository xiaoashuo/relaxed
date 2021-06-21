package com.relaxed.common.dingtalk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Yakir
 * @Topic MessageTypeEnum
 * @Description 按钮排列样式值
 * @date 2021/6/19
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum MessageTypeEnum {

	/**
	 * 文本消息
	 */
	TEXT("text", "文本"),
	/**
	 * 链接消息
	 */
	LINK("link", "链接"),
	/**
	 * markdown 消息
	 */
	MARKDOWN("markdown", "markdown"),
	/**
	 * ActionCard消息
	 */
	ACTION_CARD("actionCard", "跳转 actionCard 类型"),;

	private final String val;

	private final String desc;

}

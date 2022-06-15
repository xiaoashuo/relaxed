package com.relaxed.extend.wechat.enums;

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
	 * markdown 消息
	 */
	MARKDOWN("markdown", "markdown"),
	/**
	 * 图文消息
	 */
	NEWS("news", "图文"),

	;

	private final String val;

	private final String desc;

}

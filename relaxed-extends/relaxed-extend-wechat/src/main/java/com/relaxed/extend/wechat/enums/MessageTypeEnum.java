package com.relaxed.extend.wechat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 微信消息类型枚举。 定义了微信机器人支持的所有消息类型。
 *
 * @author Yakir
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum MessageTypeEnum {

	/**
	 * 文本消息
	 */
	TEXT("text", "文本"),

	/**
	 * Markdown格式消息
	 */
	MARKDOWN("markdown", "markdown"),

	/**
	 * 图文消息
	 */
	NEWS("news", "图文");

	private final String val;

	private final String desc;

}

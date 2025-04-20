package com.relaxed.extend.dingtalk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 钉钉消息类型枚举。 定义了钉钉机器人支持的所有消息类型。
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
	 * 链接消息
	 */
	LINK("link", "链接"),
	/**
	 * Markdown格式消息
	 */
	MARKDOWN("markdown", "markdown"),
	/**
	 * 动作卡片消息
	 */
	ACTION_CARD("actionCard", "跳转 actionCard 类型"),;

	private final String val;

	private final String desc;

}

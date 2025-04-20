package com.relaxed.common.desensitize.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 正则表达式脱敏类型枚举。
 * <p>
 * 定义了常用的正则脱敏规则，包括自定义规则和预定义规则。 每种类型都包含一个正则表达式和对应的替换规则。
 * </p>
 */
@Getter
@RequiredArgsConstructor
public enum RegexDesensitizationTypeEnum {

	/**
	 * 自定义脱敏类型。
	 * <p>
	 * 使用自定义的正则表达式和替换规则进行脱敏处理。 默认匹配任意字符，并使用六个星号替换。
	 * </p>
	 */
	CUSTOM("^[\\s\\S]*$", "******"),

	/**
	 * 邮箱脱敏规则。
	 * <p>
	 * 保留邮箱第一个字符和'@'之后的原文，中间部分用四个星号替换。 例如：12@qq.com -&gt; 1****@qq.com
	 * </p>
	 */
	EMAIL("(^.)[^@]*(@.*$)", "$1****$2"),

	/**
	 * 对称加密密码脱敏规则。
	 * <p>
	 * 保留密码的前3位和后2位，中间部分用四个星号替换。 例如：admin123456 -&gt; adm****56
	 * </p>
	 */
	ENCRYPTED_PASSWORD("(.{3}).*(.{2}$)", "$1****$2");

	/**
	 * 用于匹配需要脱敏内容的正则表达式。
	 * <p>
	 * 该表达式定义了需要被替换的文本模式。
	 * </p>
	 */
	private final String regex;

	/**
	 * 替换规则，用于指定如何替换匹配到的内容。
	 * <p>
	 * 可以使用 $1, $2 等引用正则表达式中的捕获组。
	 * </p>
	 */
	private final String replacement;

}

package com.relaxed.common.desensitize.handler;

import com.relaxed.common.desensitize.enums.RegexDesensitizationTypeEnum;

/**
 * 正则表达式脱敏处理器接口。 使用正则表达式匹配和替换来实现脱敏处理。 该接口适用于需要根据特定模式进行脱敏的场景，如手机号、身份证号等。
 *
 * @author Hccake
 * @since 1.0
 */
public class RegexDesensitizationHandler implements DesensitizationHandler {

	/**
	 * 正则脱敏处理
	 * @param origin 原文
	 * @param regex 正则匹配规则
	 * @param replacement 替换模板
	 * @return 脱敏后的字符串
	 */
	public String handle(String origin, String regex, String replacement) {
		return origin.replaceAll(regex, replacement);
	}

	/**
	 * 正则脱敏处理
	 * @param origin 原文
	 * @param typeEnum 正则脱敏枚举类型
	 * @return 脱敏后的字符串
	 */
	public String handle(String origin, RegexDesensitizationTypeEnum typeEnum) {
		return origin.replaceAll(typeEnum.getRegex(), typeEnum.getReplacement());
	}

}

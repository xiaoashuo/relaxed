package com.relaxed.common.desensitize.handler;

/**
 * 简单脱敏处理器接口。 提供了最基本的脱敏处理功能，直接将原始数据转换为脱敏后的数据。 该接口通常用于实现简单的脱敏规则，如固定长度的星号替换等。
 *
 * @author Hccake
 * @since 1.0
 */
public interface SimpleDesensitizationHandler extends DesensitizationHandler {

	/**
	 * 执行脱敏处理
	 * @param origin 原始字符串
	 * @return 脱敏处理后的字符串
	 */
	String handle(String origin);

}

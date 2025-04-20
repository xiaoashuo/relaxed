package com.relaxed.common.desensitize.functions;

import java.lang.annotation.Annotation;

/**
 * 脱敏函数接口。 定义了脱敏处理的标准方法，用于将原始数据转换为脱敏后的数据。 该接口通常与脱敏注解配合使用，用于处理特定类型的脱敏需求。
 *
 * @author Yakir
 * @since 1.0
 */
@FunctionalInterface
public interface DesensitizeFunction {

	/**
	 * 执行脱敏处理
	 * @param annotation 当前脱敏注解，包含脱敏配置信息
	 * @param value 原始数据
	 * @return 脱敏处理后的数据
	 */
	String desensitize(Annotation annotation, String value);

}

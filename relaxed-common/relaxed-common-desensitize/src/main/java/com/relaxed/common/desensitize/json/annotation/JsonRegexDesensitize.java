package com.relaxed.common.desensitize.json.annotation;

import com.relaxed.common.desensitize.enums.RegexDesensitizationTypeEnum;
import com.relaxed.common.desensitize.handler.RegexDesensitizationHandler;

import java.lang.annotation.*;

/**
 * 基于正则表达式的 Jackson 字段序列化脱敏注解。 该注解用于标记需要进行正则脱敏处理的字段，支持预定义和自定义两种脱敏方式。 预定义方式使用
 * {@link RegexDesensitizationTypeEnum} 中定义的规则， 自定义方式则通过指定正则表达式和替换规则来实现。
 *
 * @see RegexDesensitizationHandler
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonRegexDesensitize {

	/**
	 * 脱敏类型，用于指定正则处理方式。 当选择 {@link RegexDesensitizationTypeEnum#CUSTOM}
	 * 时，可以使用自定义的正则表达式和替换规则。 其他预定义类型将使用内置的脱敏规则。
	 * @return 脱敏类型枚举值
	 */
	RegexDesensitizationTypeEnum type();

	/**
	 * 自定义正则表达式，用于匹配需要脱敏的内容。 仅在 {@link #type()} 为
	 * {@link RegexDesensitizationTypeEnum#CUSTOM} 时生效。 默认匹配任意字符。
	 * @return 正则表达式
	 */
	String regex() default "^[\\s\\S]*$";

	/**
	 * 替换规则，用于指定如何替换匹配到的内容。 仅在 {@link #type()} 为
	 * {@link RegexDesensitizationTypeEnum#CUSTOM} 时生效。 默认使用六个星号替换。
	 * @return 替换字符串
	 */
	String replacement() default "******";

}

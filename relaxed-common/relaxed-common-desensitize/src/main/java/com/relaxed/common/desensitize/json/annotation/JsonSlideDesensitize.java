package com.relaxed.common.desensitize.json.annotation;

import com.relaxed.common.desensitize.enums.SlideDesensitizationTypeEnum;
import com.relaxed.common.desensitize.handler.SlideDesensitizationHandler;

import java.lang.annotation.*;

/**
 * 基于滑动窗口的 Jackson 字段序列化脱敏注解。 该注解用于标记需要进行滑动脱敏处理的字段，支持预定义和自定义两种脱敏方式。 预定义方式使用
 * {@link SlideDesensitizationTypeEnum} 中定义的规则， 自定义方式则通过指定左右明文长度和掩码字符来实现。
 *
 * @see SlideDesensitizationHandler
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonSlideDesensitize {

	/**
	 * 脱敏类型，用于指定滑动脱敏处理方式。 当选择 {@link SlideDesensitizationTypeEnum#CUSTOM}
	 * 时，可以使用自定义的左右明文长度和掩码字符。 其他预定义类型将使用内置的脱敏规则。
	 * @return 脱敏类型枚举值
	 */
	SlideDesensitizationTypeEnum type();

	/**
	 * 左侧保留的明文长度。 仅在 {@link #type()} 为 {@link SlideDesensitizationTypeEnum#CUSTOM} 时生效。
	 * 默认值为 0，表示不保留左侧明文。
	 * @return 左侧明文长度
	 */
	int leftPlainTextLen() default 0;

	/**
	 * 右侧保留的明文长度。 仅在 {@link #type()} 为 {@link SlideDesensitizationTypeEnum#CUSTOM} 时生效。
	 * 默认值为 0，表示不保留右侧明文。
	 * @return 右侧明文长度
	 */
	int rightPlainTextLen() default 0;

	/**
	 * 用于替换中间部分的掩码字符。 仅在 {@link #type()} 为 {@link SlideDesensitizationTypeEnum#CUSTOM}
	 * 时生效。 默认使用单个星号作为掩码字符。
	 * @return 掩码字符串
	 */
	String maskString() default "*";

}

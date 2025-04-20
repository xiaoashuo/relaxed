package com.relaxed.common.log.biz.annotation;

import com.relaxed.common.log.biz.extractor.DiffExtractor;
import com.relaxed.common.log.biz.extractor.SimpleTypeDiffExtractor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志差异标记注解，用于标记需要进行差异比较的字段。 通过该注解可以自定义字段的比较行为，包括： - 设置字段别名，使日志更易读 - 指定字段类型别名，用于特殊类型的处理 -
 * 控制是否忽略某些字段 - 自定义差异提取器，实现复杂对象的比较
 *
 * @author Yakir
 * @since 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogDiffTag {

	/**
	 * 字段别名，用于在日志中显示更友好的字段名称。 如果不设置，将使用字段的原始名称。 例如：将 "userName" 显示为 "用户名"
	 * @return 字段别名，默认为空字符串
	 */
	String alias() default "";

	/**
	 * 字段类型别名，用于指定字段值的特殊处理方式。 可以用来处理枚举、日期等特殊类型的显示格式。 例如：将日期类型指定为 "date"，使用特定的格式化方式
	 * @return 类型别名，默认为空字符串
	 */
	String typeAlias() default "";

	/**
	 * 是否忽略当前字段的差异比较。 当设置为 true 时，在生成差异日志时会跳过该字段。 适用于不需要记录变化的敏感字段或临时字段。
	 * @return true 表示忽略该字段，false 表示需要比较，默认为 false
	 */
	boolean ignore() default false;

	/**
	 * 差异提取器，用于自定义字段值的比较和差异提取逻辑。 可以通过实现 DiffExtractor 接口来处理复杂对象的比较。 默认使用
	 * SimpleTypeDiffExtractor 处理基本类型和字符串。
	 * @return 差异提取器的类，默认为 SimpleTypeDiffExtractor.class
	 */
	Class<? extends DiffExtractor> extractor() default SimpleTypeDiffExtractor.class;

}

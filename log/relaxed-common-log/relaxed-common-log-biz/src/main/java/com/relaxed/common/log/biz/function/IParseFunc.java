package com.relaxed.common.log.biz.function;

import com.relaxed.common.log.biz.constant.LogRecordConstants;

/**
 * 日志解析函数接口，用于定义自定义的日志解析函数。 该接口定义了日志解析函数的基本结构，包括命名空间、函数名、执行时机和具体实现。
 * 实现该接口的类可以作为自定义函数在日志模板中使用。
 *
 * @author Yakir
 * @since 1.0.0
 */
public interface IParseFunc {

	/**
	 * 获取函数的命名空间
	 * @return 函数的命名空间
	 */
	String namespace();

	/**
	 * 获取函数的名称
	 * @return 函数的名称
	 */
	String name();

	/**
	 * 获取函数的执行时机 若需要函数在方法执行前执行，设置为 {@link LogRecordConstants#BEFORE_FUNC} 即可。
	 * 否则可以不设置，默认为空字符串。
	 * @return 函数的执行时机
	 */
	default String around() {
		return "";
	}

	/**
	 * 执行函数的具体实现
	 * @param args 函数的参数数组
	 * @return 解析后的字符串结果
	 */
	String apply(Object[] args);

}

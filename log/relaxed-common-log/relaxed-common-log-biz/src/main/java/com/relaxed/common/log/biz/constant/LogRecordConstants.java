package com.relaxed.common.log.biz.constant;

/**
 * 日志记录常量类，定义了日志记录过程中使用的各种常量。 包括： 1. 模板解析相关的常量 2. 上下文键值常量 3. 时间记录常量 4. 函数执行阶段常量
 *
 * @author Yakir
 * @since 1.0.0
 */
public class LogRecordConstants {

	/**
	 * SpEL 表达式中的参数引用符号 用于在日志模板中引用方法参数，例如：#{#param}
	 */
	public static final String POUND_KEY = "#";

	/**
	 * 跟踪ID，用于关联一次请求或方法执行过程中产生的所有日志 可以通过该ID追踪完整的操作链路
	 */
	public static final String TRACE_ID = "traceId";

	/**
	 * 差异比对对象的键名 用于在上下文中存储需要进行差异比对的对象列表
	 */
	public static final String DIFF_KEY = "_diff_key";

	/**
	 * 错误信息键名 用于在上下文中存储方法执行过程中的错误信息
	 */
	public static final String ERR_MSG = "_errMsg";

	/**
	 * 方法执行结果键名 用于在上下文中存储方法的返回值
	 */
	public static final String RESULT = "_result";

	/**
	 * 方法开始执行时间键名 用于记录方法的开始执行时间戳
	 */
	public static final String S_TIME = "_stime";

	/**
	 * 方法结束执行时间键名 用于记录方法的结束执行时间戳
	 */
	public static final String E_TIME = "_etime";

	/**
	 * 前置函数执行阶段标识 表示函数在目标方法执行前被调用
	 */
	public static final String BEFORE_FUNC = "before";

	/**
	 * 后置函数执行阶段标识 表示函数在目标方法执行后被调用
	 */
	public static final String AFTER_FUNC = "after";

}

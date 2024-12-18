package com.relaxed.common.log.biz.constant;

/**
 * @author Yakir
 * @Topic LogConstants
 * @Description
 * @date 2023/12/18 13:57
 * @Version 1.0
 */
public class LogRecordConstants {

	/**
	 * #号键值，用于替换参数
	 */
	public static final String POUND_KEY = "#";

	/**
	 * 跟踪ID，用于一次请求或执行方法时，产生的各种日志间的数据关联
	 */
	public static final String TRACE_ID = "traceId";

	/**
	 * diff key
	 */
	public static final String DIFF_KEY = "_diff_key";

	/**
	 * 内置参数：错误信息
	 */
	public static final String ERR_MSG = "_errMsg";

	/**
	 * 内置参数：结果
	 */
	public static final String RESULT = "_result";

	/**
	 * 开始时间
	 */
	public static final String S_TIME = "_stime";

	/**
	 * 结束时间
	 */
	public static final String E_TIME = "_etime";

	public static final String BEFORE_FUNC = "before";

	public static final String AFTER_FUNC = "after";

}

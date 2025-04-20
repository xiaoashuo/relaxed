package com.relaxed.common.log.biz.function;

/**
 * 函数评估接口，用于执行日志记录中的自定义函数 该接口定义了函数执行的基本规范，用于在日志记录过程中评估和执行自定义函数 实现该接口的类需要提供函数执行的具体逻辑
 *
 * @author Yakir
 * @since 1.0
 */
public interface FuncEval {

	/**
	 * 执行指定的函数并返回结果 该方法用于在日志记录过程中评估和执行自定义函数
	 * @param funcName 要执行的函数名称，用于标识要调用的具体函数
	 * @param paramName 完整的参数名称，包含命名空间和参数名
	 * @param params 函数执行所需的参数数组
	 * @return 函数执行后的结果字符串
	 */
	String evalFunc(String funcName, String paramName, Object[] params);

}

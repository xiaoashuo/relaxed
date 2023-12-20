package com.relaxed.common.log.operation.discover.func;

/**
 * @author Yakir
 * @Topic FuncFilter
 * @Description
 * @date 2023/12/20 13:38
 * @Version 1.0
 */
public interface FuncEval {

	/**
	 * 函数执行器
	 * @param funcName 函数名
	 * @param paramName 全参数名
	 * @param params 普通参数
	 * @return
	 */
	String evalFunc(String funcName, String paramName, Object[] params);

}

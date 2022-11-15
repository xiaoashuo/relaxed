package com.relaxed.common.core.batch.functions;

import com.relaxed.common.core.batch.params.DataWrapper;

/**
 * @author Yakir
 * @Topic ExceptionHandler
 * @Description
 * @date 2022/11/14 18:29
 * @Version 1.0
 */
public interface ExceptionHandler<T> {

	/**
	 * 异常收集
	 * @param taskName
	 * @param dataWrapper
	 * @param throwable
	 */
	void collect(String taskName, DataWrapper<T> dataWrapper, Throwable throwable);

}

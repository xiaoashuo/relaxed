package com.relaxed.common.core.pipeline;

/**
 * @author Yakir
 * @Topic BusinessProcess
 * @Description
 * @date 2022/2/10 9:49
 * @Version 1.0
 */
public interface BusinessProcess {

	/**
	 * 当前业务处理是否支持
	 * @author yakir
	 * @date 2022/2/10 10:06
	 * @param context
	 * @return boolean true 支持 false 不支持
	 */
	boolean support(ProcessContext context);

	/**
	 * 真正处理逻辑
	 * @param context
	 */
	void process(ProcessContext context);

}

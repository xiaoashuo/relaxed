package com.relaxed.common.core.pipeline;

/**
 * @author Yakir
 * @Topic PostProcessor
 * @Description
 * @date 2022/2/10 9:59
 * @Version 1.0
 */
@FunctionalInterface
public interface PostProcessor {

	/**
	 * 后置处理
	 * @author yakir
	 * @date 2022/2/10 10:01
	 * @param processContext
	 */
	void postProcess(ProcessContext processContext);

}

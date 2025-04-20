package com.relaxed.common.core.pipeline;

/**
 * 后置处理器接口，用于在业务流程处理完成后执行额外的处理逻辑。 该接口是一个函数式接口，通常用于实现清理、日志记录、状态更新等后置操作。
 *
 * @author Yakir
 * @since 1.0
 */
@FunctionalInterface
public interface PostProcessor {

	/**
	 * 执行后置处理逻辑
	 * @param processContext 流程上下文对象，包含处理结果和状态信息
	 */
	void postProcess(ProcessContext processContext);

}

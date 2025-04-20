package com.relaxed.common.core.pipeline;

/**
 * 业务处理接口，定义了责任链模式中的处理节点。 该接口定义了业务处理的两个核心方法：支持判断和实际处理。 实现类需要根据具体业务场景实现这两个方法。
 *
 * @author Yakir
 * @since 1.0
 */
public interface BusinessProcess {

	/**
	 * 判断当前处理节点是否支持处理给定的上下文
	 * @param context 流程上下文对象
	 * @return true 表示支持处理，false 表示不支持处理
	 */
	boolean support(ProcessContext context);

	/**
	 * 执行实际的业务处理逻辑
	 * @param context 流程上下文对象，包含处理所需的数据和状态
	 */
	void process(ProcessContext context);

}

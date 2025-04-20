package com.relaxed.common.core.pipeline;

import java.util.List;

/**
 * 流程模板类，用于定义和管理业务流程的处理节点。 该类作为责任链模式的配置容器，存储了特定业务场景下的处理节点列表。
 *
 * @author Yakir
 * @since 1.0
 */
public class ProcessTemplate {

	/**
	 * 业务流程处理节点列表
	 */
	private List<BusinessProcess> processList;

	/**
	 * 获取业务流程处理节点列表
	 * @return 处理节点列表
	 */
	public List<BusinessProcess> getProcessList() {
		return processList;
	}

	/**
	 * 设置业务流程处理节点列表
	 * @param processList 处理节点列表
	 */
	public void setProcessList(List<BusinessProcess> processList) {
		this.processList = processList;
	}

}

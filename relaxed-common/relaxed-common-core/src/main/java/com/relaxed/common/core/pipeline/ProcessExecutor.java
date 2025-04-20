package com.relaxed.common.core.pipeline;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.relaxed.common.model.result.R;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 流程执行器类，负责执行和管理流程处理过程。 该类实现了责任链模式，用于处理业务流程中的多个处理节点。 支持流程模板配置、前置检查、节点处理和全局后置处理等功能。
 *
 * @author Yakir
 * @since 1.0
 */

public class ProcessExecutor {

	/**
	 * 模板映射
	 */
	private final Map<String, ProcessTemplate> templateConfig;

	private final PostProcessor globalPostProcessor;

	public ProcessExecutor(Map<String, ProcessTemplate> templateConfig) {
		this.templateConfig = templateConfig;
		this.globalPostProcessor = processContext -> {
		};
	}

	public ProcessExecutor(Map<String, ProcessTemplate> templateConfig, PostProcessor globalPostProcessor) {
		this.templateConfig = templateConfig;
		this.globalPostProcessor = globalPostProcessor;
	}

	/**
	 * 执行责任链处理流程
	 * @param context 流程上下文对象，包含处理所需的数据和状态
	 * @return 处理完成后的流程上下文对象
	 */
	public ProcessContext process(ProcessContext context) {

		/**
		 * 前置检查
		 */
		if (!preCheck(context)) {
			return context;
		}
		/**
		 * 遍历流程节点
		 */
		List<BusinessProcess> processList = templateConfig.get(context.getChannel()).getProcessList();
		for (BusinessProcess businessProcess : processList) {
			// 当前处理器不支持 直接跳过
			if (!businessProcess.support(context)) {
				continue;
			}
			// 真实业务处理
			businessProcess.process(context);
			if (context.isNeedBreak()) {
				break;
			}
		}
		// 回调处理
		globalPostProcessor.postProcess(context);
		return context;
	}

	private Boolean preCheck(ProcessContext context) {
		// 上下文
		if (context == null) {
			context = new ProcessContext();
			context.setResponse(R.failed(ProcessStatusEnum.CONTEXT_IS_NULL));
			return false;
		}

		// 业务代码
		String businessCode = context.getChannel();
		if (StrUtil.isBlank(businessCode)) {
			context.setResponse(R.failed(ProcessStatusEnum.BUSINESS_CODE_IS_NULL));
			return false;
		}

		// 执行模板
		ProcessTemplate processTemplate = templateConfig.get(businessCode);
		if (processTemplate == null) {
			context.setResponse(R.failed(ProcessStatusEnum.PROCESS_TEMPLATE_IS_NULL));
			return false;
		}

		// 执行模板列表
		List<BusinessProcess> processList = processTemplate.getProcessList();
		if (CollUtil.isEmpty(processList)) {
			context.setResponse(R.failed(ProcessStatusEnum.PROCESS_LIST_IS_NULL));
			return false;
		}

		return true;
	}

}

package com.relaxed.common.core.pipeline;

import com.relaxed.common.model.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 流程状态枚举，定义了流程处理过程中的各种状态码和消息
 *
 * @author Yakir
 * @since 1.0.0
 */
@Getter
@RequiredArgsConstructor
public enum ProcessStatusEnum implements ResultCode {

	/**
	 * pipeline
	 */
	CONTEXT_IS_NULL(60003, "流程上下文为空"), BUSINESS_CODE_IS_NULL(60004, "业务代码为空"), PROCESS_TEMPLATE_IS_NULL(60005,
			"流程模板配置为空"), PROCESS_LIST_IS_NULL(60006, "业务处理器配置为空"),

	;

	/**
	 * 响应状态
	 */
	private final Integer code;

	/**
	 * 响应编码
	 */
	private final String message;

}

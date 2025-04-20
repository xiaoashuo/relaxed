package com.relaxed.common.core.pipeline;

import com.relaxed.common.model.result.R;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 流程上下文类，用于存储和管理流程处理过程中的上下文信息
 *
 * @author Yakir
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class ProcessContext {

	/**
	 * 当前处理id
	 */
	private String processId;

	/**
	 * 标识责任链渠道
	 */
	private String channel;

	/**
	 * 存储责任链上下文数据的模型
	 */
	private ProcessModel processModel;

	/**
	 * 责任链中断标识
	 */
	private boolean needBreak;

	/**
	 * 流程响应处理结果
	 */
	private R response;

}

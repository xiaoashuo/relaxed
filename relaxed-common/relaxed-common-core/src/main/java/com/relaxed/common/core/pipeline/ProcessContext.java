package com.relaxed.common.core.pipeline;

import com.relaxed.common.model.result.R;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Yakir
 * @Topic ProcessContext
 * @Description
 * @date 2022/2/10 9:47
 * @Version 1.0
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

package com.relaxed.common.core.util.batch.core;

import lombok.Builder;
import lombok.Getter;

/**
 * BatchContext 单条数据处理上下文
 *
 * @author Yakir
 */

@Getter
@Builder
public class BatchContext<T> {

	/**
	 * 分批元数据
	 */
	private final BatchMeta batchMeta;

	/**
	 * 行内索引
	 */
	private final int rowIndex;

	/**
	 * 数据
	 */
	private final T data;

}

package com.relaxed.common.core.util.batch.core;

import lombok.Getter;
import lombok.Setter;

/**
 * 批量处理的元数据类。 包含批量处理过程中的基本信息，如批次号、起始位置和批次大小等。 用于在批量处理的各个组件之间传递批次信息。
 *
 * @author Yakir
 * @since 1.0
 */
@Getter
@Setter
public class BatchMeta {

	/**
	 * 当前批次的组号 从1开始递增，用于标识不同的批次
	 */
	private Integer groupNo;

	/**
	 * 当前批次的起始位置 由位置计算器（LocationComputer）计算得出，用于数据定位
	 */
	private Integer startIndex;

	/**
	 * 当前批次的大小 表示本批次应处理的数据量
	 */
	private Integer size;

}

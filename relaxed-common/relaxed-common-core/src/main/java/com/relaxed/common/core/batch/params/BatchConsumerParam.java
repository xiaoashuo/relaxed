package com.relaxed.common.core.batch.params;

import lombok.Data;

import java.util.List;

/**
 * @author Yakir
 * @Topic 数据消费者参数
 * @Description
 * @date 2021/7/9 12:43
 * @Version 1.0
 */
@Data
public class BatchConsumerParam<T> {

	/**
	 * 分组参数
	 */
	private BatchGroup batchGroup;

	/**
	 * 当前执行步骤起始位置
	 */
	private int currentStepPosition;

	/**
	 * 批次列表数据
	 */
	private List<T> data;

}

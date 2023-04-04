package com.relaxed.common.core.batch;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Yakir
 * @Topic BatchMeta
 * @Description
 * @date 2023/4/4 10:03
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
public class BatchLocation {

	/**
	 * 批次起始坐标
	 */
	private int batchStartPos;

	/**
	 * 当前处理行
	 */
	private int current;

	/**
	 * 批次大小
	 */
	private int batchSize;

}

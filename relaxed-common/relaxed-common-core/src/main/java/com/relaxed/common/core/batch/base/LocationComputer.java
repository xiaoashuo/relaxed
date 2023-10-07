package com.relaxed.common.core.batch.base;

/**
 * @author Yakir
 * @Topic LocationComputer
 * @Description
 * @date 2023/9/22 17:03
 * @Version 1.0
 */
@FunctionalInterface
public interface LocationComputer {

	/**
	 * 计算
	 * @param groupNo 组序号
	 * @param size 批次大小
	 * @return
	 */
	int compute(int groupNo, int size);

}

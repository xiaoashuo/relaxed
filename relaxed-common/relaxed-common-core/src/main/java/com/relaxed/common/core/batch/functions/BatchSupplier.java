package com.relaxed.common.core.batch.functions;

import java.util.List;

/**
 * @author Yakir
 * @Topic BatchSupplier
 * @Description
 * @date 2021/7/9 12:48
 * @Version 1.0
 */
public interface BatchSupplier<T> {

	/**
	 * 数据提供者
	 * @param currentStepPosition 当前索引起始位置
	 * @param size 分页批次大小
	 * @return
	 */
	List<T> apply(int currentStepPosition, int size);

}

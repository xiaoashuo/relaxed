package com.relaxed.common.core.batch.functions;

import java.util.List;

/**
 * @author Yakir
 * @Topic BatchSupplier
 * @Description
 * @date 2021/7/9 12:48
 * @Version 1.0
 */
public interface BatchSupplier<R> {

	/**
	 * 数据提供者
	 * @param currentStepPosition
	 * @param size
	 * @return
	 */
	List<R> apply(int currentStepPosition, int size);

}

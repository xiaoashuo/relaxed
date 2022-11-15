package com.relaxed.common.core.batch.functions;

import com.relaxed.common.core.batch.params.DataWrapper;

/**
 * @author Yakir
 * @Topic BatchConsumer
 * @Description
 * @date 2021/7/9 12:42
 * @Version 1.0
 */
@FunctionalInterface
public interface BatchConsumer<T> {

	/**
	 * 数据消费者
	 * @param data
	 */
	void consumer(DataWrapper<T> data);

}

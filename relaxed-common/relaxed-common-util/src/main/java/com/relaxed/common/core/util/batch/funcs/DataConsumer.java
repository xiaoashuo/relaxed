package com.relaxed.common.core.util.batch.funcs;

import com.relaxed.common.core.util.batch.meta.BatchMeta;

/**
 * @author Yakir
 * @Topic DataConsumer
 * @Description 数据消费者
 * @date 2023/9/22 15:13
 * @Version 1.0
 */
public interface DataConsumer<T> {

	/**
	 * 消费数据
	 * @param batchMeta 消费者元数据
	 * @param innerRow 内部行
	 * @param data 数据
	 * @return
	 */
	void accept(BatchMeta batchMeta, Integer innerRow, T data);

}

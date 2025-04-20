package com.relaxed.common.core.util.batch.funcs;

import com.relaxed.common.core.util.batch.core.BatchMeta;

/**
 * 批量数据处理的消费者接口。 负责处理批量任务中的单条数据，可以执行如数据库写入、远程调用等具体业务操作。
 *
 * @param <T> 待处理的数据类型
 * @author Yakir
 * @since 1.0
 */
public interface DataConsumer<T> {

	/**
	 * 消费单条数据
	 * @param batchMeta 批次元数据，包含当前批次的基本信息（如批次号、起始位置、大小等）
	 * @param innerRow 当前数据在批次内的行号（从1开始）
	 * @param data 待处理的数据对象
	 */
	void accept(BatchMeta batchMeta, Integer innerRow, T data);

}

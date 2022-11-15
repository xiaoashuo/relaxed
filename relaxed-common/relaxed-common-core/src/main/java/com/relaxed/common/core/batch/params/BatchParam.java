package com.relaxed.common.core.batch.params;

import cn.hutool.core.util.StrUtil;
import com.relaxed.common.core.batch.BatchException;
import com.relaxed.common.core.batch.functions.BatchConsumer;
import com.relaxed.common.core.batch.functions.BatchSupplier;
import com.relaxed.common.core.batch.functions.ExceptionHandler;
import lombok.Data;

/**
 * @author Yakir
 * @Topic BatchParam
 * @Description
 * @date 2021/7/10 8:08
 * @Version 1.0
 */
@Data
public class BatchParam<T> {

	/**
	 * 任务名称
	 */
	private String taskName = "default";

	/**
	 * 分组参数
	 */
	private BatchGroup batchGroup;

	/**
	 * 批处理消费者
	 */
	private BatchConsumer<T> batchConsumer;

	/**
	 * 批处理数据获取函数
	 */
	private BatchSupplier<T> batchSupplier;

	/**
	 * 是否开启异步
	 */
	private boolean async = false;

	private ExceptionHandler<T> exceptionHandler = (taskName, dataWrapper, throwable) -> {
		throw new BatchException(StrUtil.format("当前任务名称:{} 处理参数:{} ", taskName, dataWrapper), throwable);
	};

	private BatchParam() {
	}

	private BatchParam(BatchGroup batchGroup, BatchSupplier batchSupplier, BatchConsumer batchConsumer) {
		this.batchGroup = batchGroup;
		this.batchConsumer = batchConsumer;
		this.batchSupplier = batchSupplier;
	}

	public static BatchParam ofRun(BatchGroup batchGroup, BatchSupplier batchSupplier, BatchConsumer batchConsumer) {
		return new BatchParam(batchGroup, batchSupplier, batchConsumer);
	}

}

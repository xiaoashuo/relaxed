package com.relaxed.common.core.batch;

import java.util.List;

/**
 * @author Yakir
 * @Topic ITask
 * @Description
 * @date 2023/4/3 15:23
 * @Version 1.0
 */
public interface ITask<T extends BatchLocation, M> {

	/**
	 * 任务名称
	 * @return
	 */
	String taskName();

	boolean isAsync();

	BatchSupplier<T, M> supplier();

	BatchConsumer<T, M> consumer();

	void exceptionCollect(T extra, M data, Throwable throwable);

	@FunctionalInterface
	interface BatchSupplier<T, M> {

		List<M> get(T t);

	}

	@FunctionalInterface
	interface BatchConsumer<T, M> {

		void consumer(T t, M m);

	}

}

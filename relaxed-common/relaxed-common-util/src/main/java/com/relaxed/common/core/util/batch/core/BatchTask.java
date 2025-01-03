package com.relaxed.common.core.util.batch.core;

import com.relaxed.common.core.util.batch.BatchExecutor;
import com.relaxed.common.core.util.batch.funcs.DataConsumer;
import com.relaxed.common.core.util.batch.funcs.DataProvider;
import com.relaxed.common.core.util.batch.funcs.ExceptionHandler;
import com.relaxed.common.core.util.batch.funcs.LocationComputer;

import java.util.Map;

/**
 * @author Yakir
 * @Topic BatchTask
 * @Description
 * @date 2025/1/2 14:24
 * @Version 1.0
 */
public class BatchTask<T> {

	private BatchExecutor _parent;

	private DataProvider<T> provider;

	private DataConsumer<T> consumer;

	private ExceptionHandler expResolver = BatchTask::throwException;

	private LocationComputer locationComputer = BatchTask::locationComputer;

	private static int locationComputer(int groupNo, int size) {
		return (groupNo - 1) * size;
	}

	public BatchTask(BatchExecutor parent) {
		this._parent = parent;
	}

	public BatchTask<T> provider(DataProvider<T> provider) {
		this.provider = provider;
		return this;
	}

	public BatchTask<T> consumer(DataConsumer<T> consumer) {
		this.consumer = consumer;
		return this;
	}

	public BatchTask expResolver(ExceptionHandler expResolver) {
		this.expResolver = expResolver;
		return this;
	}

	public DataProvider<T> getProvider() {
		return provider;
	}

	public DataConsumer<T> getConsumer() {
		return consumer;
	}

	public ExceptionHandler getExpResolver() {
		return expResolver;
	}

	public LocationComputer getLocationComputer() {
		return locationComputer;
	}

	public BatchExecutor end() {
		return _parent;
	}

	@SuppressWarnings("unchecked")
	private static <E extends Throwable> void throwException(BatchMeta batchMeta, Map<String, Object> extParam,
			Throwable t) throws E {
		throw (E) t;
	}

}
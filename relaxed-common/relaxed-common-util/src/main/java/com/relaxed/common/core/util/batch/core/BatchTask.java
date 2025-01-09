package com.relaxed.common.core.util.batch.core;

import com.relaxed.common.core.util.batch.BatchUtil;
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

	private BatchUtil _parent;

	private DataProvider<T> provider;

	private DataConsumer<T> consumer;

	private ExceptionHandler expResolver = BatchTask::throwException;

	private LocationComputer locationComputer = BatchTask::DEFAULT_LOCATION_COMPUTER;

	private static int DEFAULT_LOCATION_COMPUTER(int groupNo, int size) {
		return (groupNo - 1) * size;
	}

	public BatchTask(BatchUtil parent) {
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

	public BatchTask<T> expResolver(ExceptionHandler expResolver) {
		this.expResolver = expResolver;
		return this;
	}

	public BatchTask<T> locationComputer(LocationComputer locationComputer) {
		this.locationComputer = locationComputer;
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

	public BatchUtil end() {
		return _parent;
	}

	@SuppressWarnings("unchecked")
	private static <E extends Throwable> void throwException(BatchMeta batchMeta, Map<String, Object> extParam,
			Throwable t) throws E {
		throw (E) t;
	}

}

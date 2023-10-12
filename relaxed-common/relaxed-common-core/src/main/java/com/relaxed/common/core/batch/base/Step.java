package com.relaxed.common.core.batch.base;

import java.util.List;
import java.util.Map;

/**
 * @author Yakir
 * @Topic Step
 * @Description
 * @date 2023/9/22 14:57
 * @Version 1.0
 */
public class Step<T> {

	/**
	 * 任务名称
	 */
	private String taskName;

	/**
	 * 是否异步
	 */
	private boolean isAsync = false;

	/**
	 * 组元信息
	 */
	private GroupMeta groupMeta;

	/**
	 * 数据提供者
	 */
	private DataProvider<T> supplier;

	/**
	 * 数据消费者
	 */
	private DataConsumer<T> consumer;

	/**
	 * 异常处理
	 */
	private ExceptionHandler exceptionHandler = Step::throwException;

	/**
	 * 坐标定位器
	 */
	private LocationComputer locationComputer = Step::locationComputer;

	public Step<T> async(boolean isAsync) {
		this.isAsync = isAsync;
		return this;
	}

	public Step<T> batch(long total, int size) {
		this.groupMeta = new GroupMeta(total, size);
		return this;
	}

	public Step<T> taskName(String taskName) {
		this.taskName = taskName;
		return this;
	}

	public Step<T> supplier(DataProvider<T> supplier) {
		this.supplier = supplier;
		return this;
	}

	public Step<T> consumer(DataConsumer<T> consumer) {
		this.consumer = consumer;
		return this;
	}

	public Step<T> exp(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
		return this;
	}

	public GroupMeta getGroupMeta() {
		return groupMeta;
	}

	public String getTaskName() {
		return taskName;
	}

	public DataProvider<T> getSupplier() {
		return supplier;
	}

	public DataConsumer<T> getConsumer() {
		return consumer;
	}

	public boolean isAsync() {
		return isAsync;
	}

	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	public LocationComputer getLocationComputer() {
		return locationComputer;
	}

	@SuppressWarnings("unchecked")
	private static <E extends Throwable> void throwException(BatchMeta batchMeta, Map<String, Object> extParam,
			Throwable t) throws E {
		throw (E) t;
	}

	private static int locationComputer(int groupNo, int size) {
		return (groupNo - 1) * size;
	}

	public List<T> getDataByProviderMeta(BatchMeta batchMeta) {
		return this.getSupplier().get(batchMeta);
	}

}

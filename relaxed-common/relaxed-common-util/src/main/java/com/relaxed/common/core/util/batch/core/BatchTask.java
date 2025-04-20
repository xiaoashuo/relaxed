package com.relaxed.common.core.util.batch.core;

import com.relaxed.common.core.util.batch.BatchUtil;
import com.relaxed.common.core.util.batch.funcs.DataConsumer;
import com.relaxed.common.core.util.batch.funcs.DataProvider;
import com.relaxed.common.core.util.batch.funcs.ExceptionHandler;
import com.relaxed.common.core.util.batch.funcs.LocationComputer;

import java.util.Map;

/**
 * 批量任务的核心配置类。 提供链式调用方式配置批量任务的核心组件，包括： 1. 数据提供者（DataProvider） 2. 数据消费者（DataConsumer） 3.
 * 异常处理器（ExceptionHandler） 4. 位置计算器（LocationComputer）
 *
 * @param <T> 处理的数据类型
 * @author Yakir
 * @since 1.0
 */
public class BatchTask<T> {

	/**
	 * 父级BatchUtil实例的引用
	 */
	private BatchUtil _parent;

	/**
	 * 数据提供者，负责获取待处理的数据
	 */
	private DataProvider<T> provider;

	/**
	 * 数据消费者，负责处理单条数据
	 */
	private DataConsumer<T> consumer;

	/**
	 * 异常处理器，默认直接抛出异常
	 */
	private ExceptionHandler expResolver = BatchTask::throwException;

	/**
	 * 位置计算器，默认使用 (groupNo - 1) * size 计算起始位置
	 */
	private LocationComputer locationComputer = BatchTask::DEFAULT_LOCATION_COMPUTER;

	/**
	 * 默认的位置计算方法
	 * @param groupNo 组号
	 * @param size 批次大小
	 * @return 计算得到的起始位置
	 */
	private static int DEFAULT_LOCATION_COMPUTER(int groupNo, int size) {
		return (groupNo - 1) * size;
	}

	/**
	 * 构造函数
	 * @param parent 父级BatchUtil实例
	 */
	public BatchTask(BatchUtil parent) {
		this._parent = parent;
	}

	/**
	 * 设置数据提供者
	 * @param provider 数据提供者实例
	 * @return 当前BatchTask实例
	 */
	public BatchTask<T> provider(DataProvider<T> provider) {
		this.provider = provider;
		return this;
	}

	/**
	 * 设置数据消费者
	 * @param consumer 数据消费者实例
	 * @return 当前BatchTask实例
	 */
	public BatchTask<T> consumer(DataConsumer<T> consumer) {
		this.consumer = consumer;
		return this;
	}

	/**
	 * 设置异常处理器
	 * @param expResolver 异常处理器实例
	 * @return 当前BatchTask实例
	 */
	public BatchTask<T> expResolver(ExceptionHandler expResolver) {
		this.expResolver = expResolver;
		return this;
	}

	/**
	 * 设置位置计算器
	 * @param locationComputer 位置计算器实例
	 * @return 当前BatchTask实例
	 */
	public BatchTask<T> locationComputer(LocationComputer locationComputer) {
		this.locationComputer = locationComputer;
		return this;
	}

	/**
	 * 获取数据提供者
	 * @return 数据提供者实例
	 */
	public DataProvider<T> getProvider() {
		return provider;
	}

	/**
	 * 获取数据消费者
	 * @return 数据消费者实例
	 */
	public DataConsumer<T> getConsumer() {
		return consumer;
	}

	/**
	 * 获取异常处理器
	 * @return 异常处理器实例
	 */
	public ExceptionHandler getExpResolver() {
		return expResolver;
	}

	/**
	 * 获取位置计算器
	 * @return 位置计算器实例
	 */
	public LocationComputer getLocationComputer() {
		return locationComputer;
	}

	/**
	 * 结束任务配置，返回父级BatchUtil实例
	 * @return 父级BatchUtil实例
	 */
	public BatchUtil end() {
		return _parent;
	}

	/**
	 * 默认的异常处理方法，直接将异常重新抛出
	 * @param batchMeta 批次元数据
	 * @param extParam 扩展参数
	 * @param t 待处理的异常
	 * @param <E> 异常类型
	 * @throws E 重新抛出的异常
	 */
	@SuppressWarnings("unchecked")
	private static <E extends Throwable> void throwException(BatchMeta batchMeta, Map<String, Object> extParam,
			Throwable t) throws E {
		throw (E) t;
	}

}

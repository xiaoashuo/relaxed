package com.relaxed.common.core.util.batch.core;

import lombok.Getter;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * BatchConfig
 *
 * @author Yakir
 */
@Getter
public class BatchConfig<T> {

	/**
	 * 任务名称
	 */
	private final String taskName;

	/**
	 * 待处理的总数据量
	 */
	private final int totalCount;

	/**
	 * 批次大小
	 */
	private final int batchSize;

	/**
	 * 是否异步
	 */
	private final boolean async;

	/**
	 * 任务执行线程池
	 */
	private Executor executor;

	/**
	 * 位置计算器，默认使用 (groupNo - 1) * size 计算起始位置
	 */
	private LocationComputer locationComputer;

	/**
	 * 数据提供者
	 */
	private final Function<BatchParam, List<T>> provider;

	/**
	 * 消费者
	 */
	private final Consumer<BatchContext<T>> consumer;

	/**
	 * 最大异常数
	 */
	private final int maxExceptions;

	/**
	 * 异常处理
	 */
	private final Consumer<List<Throwable>> exceptionHandler;

	/**
	 * 异常记录器
	 */
	private BiConsumer<BatchContext<T>, Throwable> exceptionRecordHandler;

	/**
	 * 进度监听
	 */
	private final Consumer<BatchProgress> progressListener;

	/**
	 * 是否快速失败
	 */
	private boolean failFast;

	BatchConfig(String taskName, int totalCount, int batchSize, boolean async, Executor executor,
			LocationComputer locationComputer, Function<BatchParam, List<T>> provider,
			Consumer<BatchContext<T>> consumer, int maxExceptions, Consumer<List<Throwable>> exceptionHandler,
			BiConsumer<BatchContext<T>, Throwable> exceptionRecordHandler, Consumer<BatchProgress> progressListener,
			boolean failFast) {
		this.taskName = taskName;
		this.totalCount = totalCount;
		this.batchSize = batchSize;
		this.async = async;
		this.executor = executor;
		this.locationComputer = locationComputer;
		this.provider = provider;
		this.consumer = consumer;
		this.maxExceptions = maxExceptions;
		this.exceptionHandler = exceptionHandler;
		this.exceptionRecordHandler = exceptionRecordHandler;
		this.progressListener = progressListener;
		this.failFast = failFast;
	}

	public static <T> BatchConfigBuilder<T> builder(Class<T> t) {
		return new BatchConfigBuilder(t);
	}

	public static <T> BatchConfigBuilder<T> builder() {
		return new BatchConfigBuilder();
	}

	public static class BatchConfigBuilder<T> {

		private String taskName;

		private int totalCount;

		private int batchSize;

		private boolean async;

		private Executor executor;

		private LocationComputer locationComputer;

		private Function<BatchParam, List<T>> provider;

		private Consumer<BatchContext<T>> consumer;

		private int maxExceptions;

		private Consumer<List<Throwable>> exceptionHandler;

		private Consumer<BatchProgress> progressListener;

		private BiConsumer<BatchContext<T>, Throwable> exceptionRecordHandler;

		private boolean failFast;

		BatchConfigBuilder() {
		}

		public BatchConfigBuilder(Class<T> t) {
		}

		public BatchConfigBuilder<T> taskName(String taskName) {
			this.taskName = taskName;
			return this;
		}

		public BatchConfigBuilder<T> totalCount(int totalCount) {
			this.totalCount = totalCount;
			return this;
		}

		public BatchConfigBuilder<T> batchSize(int batchSize) {
			this.batchSize = batchSize;
			return this;
		}

		public BatchConfigBuilder<T> async(boolean async) {
			this.async = async;
			return this;
		}

		public BatchConfigBuilder<T> executor(Executor executor) {
			this.executor = executor;
			return this;
		}

		public BatchConfigBuilder<T> locationComputer(LocationComputer locationComputer) {
			this.locationComputer = locationComputer;
			return this;
		}

		public BatchConfigBuilder<T> provider(Function<BatchParam, List<T>> provider) {
			this.provider = provider;
			return this;
		}

		public BatchConfigBuilder<T> consumer(Consumer<BatchContext<T>> consumer) {
			this.consumer = consumer;
			return this;
		}

		public BatchConfigBuilder<T> maxExceptions(int maxExceptions) {
			this.maxExceptions = maxExceptions;
			return this;
		}

		public BatchConfigBuilder<T> exceptionHandler(Consumer<List<Throwable>> exceptionHandler) {
			this.exceptionHandler = exceptionHandler;
			return this;
		}

		public BatchConfigBuilder<T> exceptionRecordHandler(
				BiConsumer<BatchContext<T>, Throwable> exceptionRecordHandler) {
			this.exceptionRecordHandler = exceptionRecordHandler;
			return this;
		}

		public BatchConfigBuilder<T> progressListener(Consumer<BatchProgress> progressListener) {
			this.progressListener = progressListener;
			return this;
		}

		public BatchConfigBuilder<T> failFast(boolean failFast) {
			this.failFast = failFast;
			return this;
		}

		public BatchConfig<T> build() {
			return new BatchConfig(this.taskName, this.totalCount, this.batchSize, this.async, this.executor,
					this.locationComputer, this.provider, this.consumer, this.maxExceptions, this.exceptionHandler,
					this.exceptionRecordHandler, this.progressListener, this.failFast);
		}

	}

}

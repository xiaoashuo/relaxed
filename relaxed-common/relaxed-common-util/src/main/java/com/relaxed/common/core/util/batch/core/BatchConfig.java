package com.relaxed.common.core.util.batch.core;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.Executor;
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
	private final Function<BatchMeta, List<T>> provider;

	/**
	 * 消费者
	 */
	private final Consumer<BatchContext<T>> consumer;

	/**
	 * 异常处理
	 */
	private final Consumer<List<Throwable>> exceptionHandler;

	/**
	 * 进度监听
	 */
	private final Consumer<BatchProgress> progressListener;

	/**
	 * 是否快速失败
	 */
	private boolean failFast;

	BatchConfig(String taskName, int totalCount, int batchSize, boolean async, Executor executor,
			LocationComputer locationComputer, Function<BatchMeta, List<T>> provider,
			Consumer<BatchContext<T>> consumer, Consumer<List<Throwable>> exceptionHandler,
			Consumer<BatchProgress> progressListener, boolean failFast) {
		this.taskName = taskName;
		this.totalCount = totalCount;
		this.batchSize = batchSize;
		this.async = async;
		this.executor = executor;
		this.locationComputer = locationComputer;
		this.provider = provider;
		this.consumer = consumer;
		this.exceptionHandler = exceptionHandler;
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

		private Function<BatchMeta, List<T>> provider;

		private Consumer<BatchContext<T>> consumer;

		private Consumer<List<Throwable>> exceptionHandler;

		private Consumer<BatchProgress> progressListener;

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

		public BatchConfigBuilder<T> provider(Function<BatchMeta, List<T>> provider) {
			this.provider = provider;
			return this;
		}

		public BatchConfigBuilder<T> consumer(Consumer<BatchContext<T>> consumer) {
			this.consumer = consumer;
			return this;
		}

		public BatchConfigBuilder<T> exceptionHandler(Consumer<List<Throwable>> exceptionHandler) {
			this.exceptionHandler = exceptionHandler;
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
					this.locationComputer, this.provider, this.consumer, this.exceptionHandler, this.progressListener,
					this.failFast);
		}

		public String toString() {
			return "BatchConfig.BatchConfigBuilder(taskName=" + this.taskName + ", totalCount=" + this.totalCount
					+ ", batchSize=" + this.batchSize + ", async=" + this.async + ", executor=" + this.executor
					+ ", locationComputer=" + this.locationComputer + ", provider=" + this.provider + ", consumer="
					+ this.consumer + ", exceptionHandler=" + this.exceptionHandler + ", progressListener="
					+ this.progressListener + ", failFast=" + this.failFast + ")";
		}

	}

}

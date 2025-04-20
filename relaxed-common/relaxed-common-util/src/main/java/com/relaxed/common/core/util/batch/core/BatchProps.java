package com.relaxed.common.core.util.batch.core;

import com.relaxed.common.core.util.batch.BatchUtil;

/**
 * 批量处理任务的属性配置类。 提供链式调用方式配置批量任务的基本属性，包括： 1. 任务名称 2. 同步/异步执行模式 3. 调试日志开关
 *
 * @author Yakir
 * @since 1.0
 */
public class BatchProps {

	/**
	 * 父级BatchUtil实例的引用
	 */
	private BatchUtil _parent;

	/**
	 * 任务名称，用于标识和区分不同的批量任务
	 */
	private String taskName;

	/**
	 * 是否使用异步模式执行任务
	 */
	private boolean isAsync;

	/**
	 * 是否启用调试日志，默认为false
	 */
	private boolean debugLog = false;

	/**
	 * 构造函数
	 * @param parent 父级BatchUtil实例
	 */
	public BatchProps(BatchUtil parent) {
		this._parent = parent;
	}

	/**
	 * 设置任务名称
	 * @param taskName 任务名称
	 * @return 当前BatchProps实例
	 */
	public BatchProps taskName(String taskName) {
		this.taskName = taskName;
		return this;
	}

	/**
	 * 设置是否启用调试日志
	 * @param debugLog 是否启用调试日志
	 * @return 当前BatchProps实例
	 */
	public BatchProps debugLog(boolean debugLog) {
		this.debugLog = debugLog;
		return this;
	}

	/**
	 * 设置是否使用异步模式
	 * @param isAsync 是否使用异步模式
	 * @return 当前BatchProps实例
	 */
	public BatchProps async(boolean isAsync) {
		this.isAsync = isAsync;
		return this;
	}

	/**
	 * 获取任务名称
	 * @return 任务名称
	 */
	public String getTaskName() {
		return taskName;
	}

	/**
	 * 判断是否为异步模式
	 * @return true表示异步模式，false表示同步模式
	 */
	public boolean isAsync() {
		return isAsync;
	}

	/**
	 * 判断是否启用调试日志
	 * @return true表示启用调试日志，false表示禁用
	 */
	public boolean isDebugLog() {
		return debugLog;
	}

	/**
	 * 结束属性配置，返回父级BatchUtil实例
	 * @return 父级BatchUtil实例
	 */
	public BatchUtil end() {
		return _parent;
	}

}

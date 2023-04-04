package com.relaxed.common.core.batch;

import java.util.concurrent.Executor;

/**
 * @author Yakir
 * @Topic BatchConfig
 * @Description
 * @date 2023/4/3 14:50
 * @Version 1.0
 */

public class BatchConfig {

	/**
	 * 执行器
	 */
	private Executor executor;

	/**
	 * 线程安全检测提醒 默认为true 默认切割线程过大会增加提醒
	 */
	private boolean threadSafeCheck = false;

	private int limitMaxThread = 10;

	private BatchUtil _parent;

	public BatchConfig(BatchUtil batchUtil) {
		this._parent = batchUtil;
	}

	public BatchUtil end() {
		return this._parent;
	}

	public BatchConfig executor(Executor executor) {
		this.executor = executor;
		return this;
	}

	public BatchConfig limitMaxThread(int limitMaxThread) {
		this.limitMaxThread = limitMaxThread;
		return this;
	}

	public BatchConfig threadSafeCheck(boolean threadSafeCheck) {
		this.threadSafeCheck = threadSafeCheck;
		return this;
	}

	public int getLimitMaxThread() {
		return limitMaxThread;
	}

	public Executor getExecutor() {
		return executor;
	}

	public boolean isThreadSafeCheck() {
		return threadSafeCheck;
	}

}

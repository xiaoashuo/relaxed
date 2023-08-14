package com.relaxed.common.core.util.batch;

import java.util.concurrent.Executor;

/**
 * @author Yakir
 * @Topic BatchExecutor
 * @Description
 * @date 2023/8/9 13:49
 * @Version 1.0
 */
public class BatchExecutor {

	/**
	 * 线程执行器
	 */
	private Executor executor;

	/**
	 * 线程安全检测提醒 默认为true 默认切割线程过大会增加提醒
	 */
	private boolean threadSafeCheck = false;

	private int limitMaxThread = 10;

	public Executor getExecutor() {
		return executor;
	}

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

	public boolean isThreadSafeCheck() {
		return threadSafeCheck;
	}

	public void setThreadSafeCheck(boolean threadSafeCheck) {
		this.threadSafeCheck = threadSafeCheck;
	}

	public int getLimitMaxThread() {
		return limitMaxThread;
	}

	public void setLimitMaxThread(int limitMaxThread) {
		this.limitMaxThread = limitMaxThread;
	}

}

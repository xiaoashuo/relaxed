package com.relaxed.common.core.util.batch.core;

import com.relaxed.common.core.util.batch.BatchUtil;

/**
 * @author Yakir
 * @Topic BatchProps
 * @Description
 * @date 2025/1/2 14:27
 * @Version 1.0
 */
public class BatchProps {

	private BatchUtil _parent;

	private String taskName;

	private boolean isAsync;

	private boolean debugLog = false;

	public BatchProps(BatchUtil parent) {
		this._parent = parent;
	}

	public BatchProps taskName(String taskName) {
		this.taskName = taskName;
		return this;
	}

	public BatchProps debugLog(boolean debugLog) {
		this.debugLog = debugLog;
		return this;
	}

	public BatchProps async(boolean isAsync) {
		this.isAsync = isAsync;
		return this;
	}

	public String getTaskName() {
		return taskName;
	}

	public boolean isAsync() {
		return isAsync;
	}

	public boolean isDebugLog() {
		return debugLog;
	}

	public BatchUtil end() {
		return _parent;
	}

}

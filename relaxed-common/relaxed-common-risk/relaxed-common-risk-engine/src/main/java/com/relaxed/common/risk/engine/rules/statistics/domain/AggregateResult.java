package com.relaxed.common.risk.engine.rules.statistics.domain;

/**
 * @author Yakir
 * @Topic SimpleAggregateResult
 * @Description
 * @date 2021/8/30 14:48
 * @Version 1.0
 */
public class AggregateResult<T> {

	private T executeResult;

	public AggregateResult(T executeResult) {
		this.executeResult = executeResult;
	}

	public T getExecuteResult() {
		return executeResult;
	}

}

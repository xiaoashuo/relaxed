package com.relaxed.common.risk.engine.rules.statistics.executor;

import com.relaxed.common.risk.engine.rules.statistics.AggregateExecutor;
import com.relaxed.common.risk.engine.rules.statistics.enums.IAggregateFunction;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Yakir
 * @Topic AggregateExecutorDiscover
 * @Description 聚合函数执行器发现者
 * @date 2021/8/30 14:59
 * @Version 1.0
 */
@RequiredArgsConstructor
public class AggregateExecutorDiscover {

	private Map<String, AggregateExecutor> AGGREGATE_EXECUTOR_HOLDER = new HashMap<>();

	public void setAggregateExecutorHolder(Map<String, AggregateExecutor> map) {
		AGGREGATE_EXECUTOR_HOLDER = map;
	}

	/**
	 * 放入聚合执行者
	 * @author yakir
	 * @date 2021/8/30 15:08
	 * @param aggregateExecutor
	 */
	public void putAggregateExecutor(AggregateExecutor aggregateExecutor) {
		AGGREGATE_EXECUTOR_HOLDER.put(aggregateExecutor.function().getFunction(), aggregateExecutor);
	}

	public AggregateExecutor discover(IAggregateFunction IAggregateFunction) {
		return Objects.requireNonNull(AGGREGATE_EXECUTOR_HOLDER.get(IAggregateFunction.getFunction()));
	}

}

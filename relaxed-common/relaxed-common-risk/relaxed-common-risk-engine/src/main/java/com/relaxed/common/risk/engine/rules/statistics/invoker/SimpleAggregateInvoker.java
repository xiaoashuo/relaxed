package com.relaxed.common.risk.engine.rules.statistics.invoker;

import com.relaxed.common.risk.engine.rules.statistics.AggregateExecutor;
import com.relaxed.common.risk.engine.rules.statistics.AggregateInvoker;
import com.relaxed.common.risk.engine.rules.statistics.domain.AggregateParam;
import com.relaxed.common.risk.engine.rules.statistics.domain.AggregateResult;
import com.relaxed.common.risk.engine.rules.statistics.enums.AggregateFunction;
import com.relaxed.common.risk.engine.rules.statistics.executor.AggregateExecutorDiscover;
import lombok.RequiredArgsConstructor;

/**
 * @author Yakir
 * @Topic SimpleAggregateInvoker
 * @Description
 * @date 2021/8/30 14:43
 * @Version 1.0
 */
@RequiredArgsConstructor
public class SimpleAggregateInvoker implements AggregateInvoker {

	private final AggregateExecutorDiscover aggregateExecutorDiscover;

	@Override
	public <T extends AggregateParam> T convert(T aggregateParam) {
		return aggregateParam;
	}

	@Override
	public <T extends AggregateParam> AggregateResult invoke(AggregateFunction function, T commandParam) {
		AggregateExecutor executor = aggregateExecutorDiscover.discover(function);
		Object result = executor.execute(commandParam);
		return new AggregateResult(result);
	}

}

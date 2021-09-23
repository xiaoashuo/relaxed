package com.relaxed.common.risk.engine.rules.statistics.invoker;

import com.relaxed.common.risk.engine.rules.EvaluateContext;
import com.relaxed.common.risk.engine.rules.statistics.AggregateExecutor;
import com.relaxed.common.risk.engine.rules.statistics.AggregateInvoker;
import com.relaxed.common.risk.engine.rules.statistics.domain.IAggregateParam;
import com.relaxed.common.risk.engine.rules.statistics.domain.AggregateParamBO;
import com.relaxed.common.risk.engine.rules.statistics.domain.AggregateResult;
import com.relaxed.common.risk.engine.rules.statistics.enums.IAggregateFunction;
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
	public <T extends IAggregateParam> T buildContext(EvaluateContext evaluateContext,
			AggregateParamBO aggregateParamBO) {
		return (T) aggregateParamBO;
	}

	@Override
	public <T extends IAggregateParam> AggregateResult invoke(IAggregateFunction function, T commandParam) {
		AggregateExecutor executor = aggregateExecutorDiscover.discover(function);
		Object result = executor.execute(commandParam);
		return new AggregateResult(result);
	}

}

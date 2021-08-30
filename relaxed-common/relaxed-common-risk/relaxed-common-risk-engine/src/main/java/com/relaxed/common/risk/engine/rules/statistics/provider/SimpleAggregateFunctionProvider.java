package com.relaxed.common.risk.engine.rules.statistics.provider;

import com.relaxed.common.risk.engine.rules.statistics.enums.AggregateEnum;
import com.relaxed.common.risk.engine.rules.statistics.enums.AggregateFunction;

import java.util.Objects;

/**
 * @author Yakir
 * @Topic SimpleAggregateFunctionProvider
 * @Description
 * @date 2021/8/30 15:55
 * @Version 1.0
 */
public class SimpleAggregateFunctionProvider implements AggregateFunctionProvider {

	@Override
	public AggregateFunction provide(Integer order) {
		return Objects.requireNonNull(AggregateEnum.of(order));
	}

}

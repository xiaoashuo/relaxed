package com.relaxed.common.risk.engine.rules.statistics.provider;

import com.relaxed.common.risk.engine.rules.statistics.enums.AggregateFunction;

/**
 * @author Yakir
 * @Topic AggregateFunctionProvider
 * @Description
 * @date 2021/8/30 15:54
 * @Version 1.0
 */
public interface AggregateFunctionProvider {

	/**
	 * 根据函数数据提供 聚合函数
	 * @author yakir
	 * @date 2021/8/30 15:55
	 * @param order
	 * @return com.relaxed.common.risk.engine.rules.statistics.enums.AggregateFunction
	 */
	AggregateFunction provide(Integer order);

}

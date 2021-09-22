package com.relaxed.common.risk.engine.rules.statistics;

import com.relaxed.common.risk.engine.rules.statistics.domain.IAggregateParam;
import com.relaxed.common.risk.engine.rules.statistics.enums.AggregateFunction;

/**
 * @author Yakir
 * @Topic AggregateExecutor
 * @Description 聚合执行者
 * @date 2021/8/30 14:40
 * @Version 1.0
 */
public interface AggregateExecutor<T extends IAggregateParam, R> {

	/**
	 * 函数类型
	 * @author yakir
	 * @date 2021/8/30 14:50
	 * @return java.lang.Integer
	 */
	AggregateFunction function();

	/**
	 * 执行命令
	 * @author yakir
	 * @date 2021/8/30 15:18
	 * @param aggregateParam
	 * @return R
	 */
	R execute(T aggregateParam);

}

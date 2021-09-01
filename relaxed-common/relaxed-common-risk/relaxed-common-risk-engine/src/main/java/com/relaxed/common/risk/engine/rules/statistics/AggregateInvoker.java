package com.relaxed.common.risk.engine.rules.statistics;

import com.relaxed.common.risk.engine.rules.EvaluateContext;
import com.relaxed.common.risk.engine.rules.statistics.domain.AggregateParam;
import com.relaxed.common.risk.engine.rules.statistics.domain.AggregateParamBO;
import com.relaxed.common.risk.engine.rules.statistics.domain.AggregateResult;
import com.relaxed.common.risk.engine.rules.statistics.enums.AggregateFunction;

/**
 * @author Yakir
 * @Topic AggregateInvoker
 * @Description 聚合调用者
 * @date 2021/8/30 14:39
 * @Version 1.0
 */
public interface AggregateInvoker {

	/**
	 * 转换上下文参数
	 * @author yakir
	 * @date 2021/8/30 16:03
	 * @param evaluateContext
	 * @param aggregateParamBO
	 * @return T
	 */
	<T extends AggregateParam> T buildContext(EvaluateContext evaluateContext, AggregateParamBO aggregateParamBO);

	/**
	 * 执行命令
	 * @author yakir
	 * @date 2021/8/30 14:42
	 * @param function 命令序列
	 * @param commandParam
	 * @return AggregateResult
	 */
	<T extends AggregateParam> AggregateResult invoke(AggregateFunction function, T commandParam);

}

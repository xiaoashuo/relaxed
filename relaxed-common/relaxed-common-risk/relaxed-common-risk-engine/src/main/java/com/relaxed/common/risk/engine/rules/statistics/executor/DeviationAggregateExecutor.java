package com.relaxed.common.risk.engine.rules.statistics.executor;

import com.relaxed.common.risk.engine.manage.ModelEventManageService;
import com.relaxed.common.risk.engine.rules.statistics.domain.AggregateParam;
import com.relaxed.common.risk.engine.rules.statistics.enums.AggregateEnum;
import com.relaxed.common.risk.engine.rules.statistics.enums.AggregateFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author Yakir
 * @Topic DeviationAggregateExecutor
 * @Description 偏离率
 * @date 2021/8/30 14:49
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class DeviationAggregateExecutor extends AbstractAggregateExecutor<BigDecimal> {

	private final ModelEventManageService modelEventManageService;

	@Override
	public AggregateFunction function() {
		return AggregateEnum.AVERAGE;
	}

	@Override
	public BigDecimal execute(AggregateParam aggregateParam) {
		return modelEventManageService.deviation(aggregateParam);

	}

}

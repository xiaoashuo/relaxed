package com.relaxed.common.risk.engine.rules.statistics.executor;

import com.relaxed.common.risk.engine.manage.ModelEventManageService;
import com.relaxed.common.risk.engine.rules.statistics.domain.AggregateParamBO;
import com.relaxed.common.risk.engine.rules.statistics.enums.AggregateEnum;
import com.relaxed.common.risk.engine.rules.statistics.enums.AggregateFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author Yakir
 * @Topic AverageAggregateExecutor
 * @Description
 * @date 2021/8/30 14:49
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AverageAggregateExecutor extends AbstractAggregateExecutor<AggregateParamBO, BigDecimal> {

	private final ModelEventManageService modelEventManageService;

	@Override
	public AggregateFunction function() {
		return AggregateEnum.AVERAGE;
	}

	@Override
	public BigDecimal execute(AggregateParamBO aggregateParamBO) {
		return modelEventManageService.average(aggregateParamBO.getModelId(), aggregateParamBO.getSearchFieldName(),
				aggregateParamBO.getSearchFieldVal(), aggregateParamBO.getRefDateFieldName(),
				aggregateParamBO.getBeginDate(), aggregateParamBO.getRefDateFieldVal(),
				aggregateParamBO.getFunctionFieldName());

	}

}
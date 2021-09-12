package com.relaxed.common.risk.engine.rules.statistics.executor;

import com.relaxed.common.risk.engine.service.ModelEventManageService;
import com.relaxed.common.risk.engine.rules.statistics.AggregateExecutor;
import com.relaxed.common.risk.engine.rules.statistics.domain.AggregateParamBO;
import com.relaxed.common.risk.engine.rules.statistics.enums.AggregateEnum;
import com.relaxed.common.risk.engine.rules.statistics.enums.AggregateFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Yakir
 * @Topic DistinctCountAggregateExecutor
 * @Description
 * @date 2021/8/30 16:36
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class DistinctCountAggregateExecutor implements AggregateExecutor<AggregateParamBO, Long> {

	private final ModelEventManageService modelEventManageService;

	@Override
	public AggregateFunction function() {
		return AggregateEnum.DISTINCT_COUNT;
	}

	@Override
	public Long execute(AggregateParamBO aggregateParamBO) {
		return modelEventManageService.distinctCount(aggregateParamBO.getModelId(),
				aggregateParamBO.getSearchFieldName(), aggregateParamBO.getSearchFieldVal(),
				aggregateParamBO.getRefDateFieldName(), aggregateParamBO.getBeginDate(),
				aggregateParamBO.getRefDateFieldVal(), aggregateParamBO.getFunctionFieldName());
	}

}

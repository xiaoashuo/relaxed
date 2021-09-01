package com.relaxed.common.risk.engine.rules.statistics.executor;

import com.relaxed.common.risk.engine.manage.ModelEventManageService;
import com.relaxed.common.risk.engine.rules.statistics.domain.AggregateParamBO;
import com.relaxed.common.risk.engine.rules.statistics.enums.AggregateEnum;
import com.relaxed.common.risk.engine.rules.statistics.enums.AggregateFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Yakir
 * @Topic CountAggregateExecutor
 * @Description
 * @date 2021/8/30 14:49
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class CountAggregateExecutor extends AbstractAggregateExecutor<AggregateParamBO, Long> {

	private final ModelEventManageService modelEventManageService;

	@Override
	public AggregateFunction function() {
		return AggregateEnum.COUNT;
	}

	@Override
	public Long execute(AggregateParamBO aggregateParamBO) {
		return modelEventManageService.count(aggregateParamBO.getModelId(), aggregateParamBO.getSearchFieldName(),
				aggregateParamBO.getSearchFieldVal(), aggregateParamBO.getRefDateFieldName(),
				aggregateParamBO.getBeginDate(), aggregateParamBO.getRefDateFieldVal());

	}

}

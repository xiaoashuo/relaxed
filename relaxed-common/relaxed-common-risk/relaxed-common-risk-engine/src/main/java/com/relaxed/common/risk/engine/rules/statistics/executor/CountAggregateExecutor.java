package com.relaxed.common.risk.engine.rules.statistics.executor;

import com.relaxed.common.risk.engine.manage.ModelEventManageService;
import com.relaxed.common.risk.engine.rules.statistics.AggregateExecutor;
import com.relaxed.common.risk.engine.rules.statistics.domain.AggregateParam;
import com.relaxed.common.risk.engine.rules.statistics.enums.AggregateEnum;
import com.relaxed.common.risk.engine.rules.statistics.enums.AggregateFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author Yakir
 * @Topic CountAggregateExecutor
 * @Description
 * @date 2021/8/30 14:49
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class CountAggregateExecutor extends AbstractAggregateExecutor<Long> {

	private final ModelEventManageService modelEventManageService;

	@Override
	public AggregateFunction function() {
		return AggregateEnum.COUNT;
	}

	@Override
	public Long execute(AggregateParam aggregateParam) {
		return modelEventManageService.count(aggregateParam.getModelId(), aggregateParam.getSearchFieldName(),
				aggregateParam.getSearchFieldVal(), aggregateParam.getRefDateFieldName(), aggregateParam.getBeginDate(),
				aggregateParam.getRefDateFieldVal());

	}

}

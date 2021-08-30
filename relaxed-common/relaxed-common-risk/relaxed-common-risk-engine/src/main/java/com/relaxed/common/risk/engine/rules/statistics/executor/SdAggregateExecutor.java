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
 * @Topic SdAggregateExecutor
 * @Description //TODO 标准差未完成
 * @date 2021/8/30 14:49
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class SdAggregateExecutor extends AbstractAggregateExecutor<BigDecimal> {

	private final ModelEventManageService modelEventManageService;

	@Override
	public AggregateFunction function() {
		return AggregateEnum.SD;
	}

	@Override
	public BigDecimal execute(AggregateParam aggregateParam) {
		return modelEventManageService.sd(aggregateParam.getModelId(), aggregateParam.getSearchFieldName(),
				aggregateParam.getSearchFieldVal(), aggregateParam.getRefDateFieldName(), aggregateParam.getBeginDate(),
				aggregateParam.getRefDateFieldVal(), aggregateParam.getFunctionFieldName(),
				aggregateParam.getFunctionFieldType());

	}

}

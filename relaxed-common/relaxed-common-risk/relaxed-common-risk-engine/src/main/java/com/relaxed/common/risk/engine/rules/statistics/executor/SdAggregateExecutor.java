package com.relaxed.common.risk.engine.rules.statistics.executor;

import com.relaxed.common.risk.engine.service.ModelEventManageService;
import com.relaxed.common.risk.engine.rules.statistics.domain.AggregateParamBO;
import com.relaxed.common.risk.engine.rules.statistics.enums.AggregateEnum;
import com.relaxed.common.risk.engine.rules.statistics.enums.IAggregateFunction;
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
public class SdAggregateExecutor extends AbstractAggregateExecutor<AggregateParamBO, BigDecimal> {

	private final ModelEventManageService modelEventManageService;

	@Override
	public IAggregateFunction function() {
		return AggregateEnum.SD;
	}

	@Override
	public BigDecimal execute(AggregateParamBO aggregateParamBO) {
		return modelEventManageService.sd(aggregateParamBO);

	}

}

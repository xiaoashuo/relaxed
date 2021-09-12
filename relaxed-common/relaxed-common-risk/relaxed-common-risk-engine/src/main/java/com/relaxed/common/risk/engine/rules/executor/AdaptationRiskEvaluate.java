package com.relaxed.common.risk.engine.rules.executor;

import com.relaxed.common.risk.engine.config.EngineProperties;
import com.relaxed.common.risk.engine.service.ModelConfManageService;
import com.relaxed.common.risk.model.vo.ModelConfVO;
import com.relaxed.common.risk.model.vo.ModelVO;
import com.relaxed.common.risk.engine.rules.AbstractRiskEvaluate;
import com.relaxed.common.risk.engine.rules.EvaluateContext;
import com.relaxed.common.risk.engine.rules.EvaluateReport;
import com.relaxed.common.risk.engine.rules.machine.Estimator;
import com.relaxed.common.risk.engine.rules.machine.EstimatorHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yakir
 * @Topic AdaptationRiskEvaluate
 * @Description 机器学习适配
 * @date 2021/8/29 17:45
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AdaptationRiskEvaluate extends AbstractRiskEvaluate {

	private static final String ADAPTATIONS = "adaptations";

	private final ModelConfManageService modelConfManageService;

	private final EstimatorHolder estimatorHolder;

	@Override
	public String getName() {
		return ADAPTATIONS;
	}

	@Override
	public boolean doEval(EvaluateContext evaluateContext, EvaluateReport evaluateReport) {
		ModelVO modelVo = evaluateContext.getModelVo();
		Map<String, Object> extParam = new HashMap<>();
		if (!EngineProperties.getMachineLearning()) {
			// 未启动机器学习直接返回
			evaluateReport.putEvaluateMap(ADAPTATIONS, extParam);
			return true;
		}
		// 启动机器学习
		ModelConfVO modelConfVO = modelConfManageService.getByModelId(modelVo.getId());
		Estimator estimator = estimatorHolder.get(modelConfVO.getType());
		float score = estimator.predict(modelConfVO, evaluateContext, evaluateReport);
		extParam.put("score", score);
		evaluateReport.putEvaluateMap(ADAPTATIONS, extParam);
		return true;
	}

}

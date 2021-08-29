package com.relaxed.common.risk.engine.rules.executor;

import com.relaxed.common.risk.engine.rules.AbstractRiskEvaluate;
import com.relaxed.common.risk.engine.rules.EvaluateContext;
import com.relaxed.common.risk.engine.rules.EvaluateReport;

/**
 * @author Yakir
 * @Topic AdaptationRiskEvaluate
 * @Description 策略评估
 * @date 2021/8/29 17:45
 * @Version 1.0
 */
public class AdaptationRiskEvaluate extends AbstractRiskEvaluate {

	@Override
	public boolean doEval(EvaluateContext evaluateContext, EvaluateReport evaluateReport) {
		System.out.println("开始执行机器学习适配风险评估");
		return true;
	}

}

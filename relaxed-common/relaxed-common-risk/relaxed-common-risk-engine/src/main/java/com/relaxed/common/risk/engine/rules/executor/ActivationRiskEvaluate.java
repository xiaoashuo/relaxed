package com.relaxed.common.risk.engine.rules.executor;

import com.relaxed.common.risk.engine.rules.AbstractRiskEvaluate;
import com.relaxed.common.risk.engine.rules.EvaluateContext;
import com.relaxed.common.risk.engine.rules.EvaluateReport;

/**
 * @author Yakir
 * @Topic ActivationRiskEvaluate
 * @Description 机器学习评估
 * @date 2021/8/29 17:47
 * @Version 1.0
 */
public class ActivationRiskEvaluate extends AbstractRiskEvaluate {

	@Override
	public boolean doEval(EvaluateContext evaluateContext, EvaluateReport evaluateReport) {
		System.out.println("开始执行策略风险评估");
		return false;
	}

}

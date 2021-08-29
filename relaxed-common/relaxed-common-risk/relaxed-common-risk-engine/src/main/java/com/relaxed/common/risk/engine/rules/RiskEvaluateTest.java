package com.relaxed.common.risk.engine.rules;

import com.relaxed.common.risk.engine.rules.executor.AbstractionRiskEvaluate;
import com.relaxed.common.risk.engine.rules.executor.ActivationRiskEvaluate;
import com.relaxed.common.risk.engine.rules.executor.AdaptationRiskEvaluate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yakir
 * @Topic RiskEvaluateTest
 * @Description
 * @date 2021/8/29 17:48
 * @Version 1.0
 */
public class RiskEvaluateTest {

	public static void main(String[] args) {
		List<RiskEvaluate> riskEvaluates = new ArrayList<>();
		riskEvaluates.add(new AbstractionRiskEvaluate());
		riskEvaluates.add(new ActivationRiskEvaluate());
		riskEvaluates.add(new AdaptationRiskEvaluate());
		RiskEvaluateChain riskEvaluateChain = new RiskEvaluateChain(riskEvaluates);
		boolean b = riskEvaluateChain.eval(new EvaluateContext(), new EvaluateReport());
		System.out.println(b);

	}

}

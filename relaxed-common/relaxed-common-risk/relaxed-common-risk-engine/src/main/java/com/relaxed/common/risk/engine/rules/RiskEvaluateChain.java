package com.relaxed.common.risk.engine.rules;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yakir
 * @Topic RiskEvaluateChain
 * @Description
 * @date 2021/8/29 17:18
 * @Version 1.0
 */
@Slf4j
public class RiskEvaluateChain {

	private int position = -1;

	private List<RiskEvaluate> riskEvaluates = new ArrayList<>();

	public RiskEvaluateChain() {
	}

	public RiskEvaluateChain(List<RiskEvaluate> riskEvaluates) {
		this.riskEvaluates = riskEvaluates;
	}

	public void setRiskEvaluates(List<RiskEvaluate> riskEvaluates) {
		this.riskEvaluates = riskEvaluates;
	}

	public void addRiskEvaluate(RiskEvaluate riskEvaluate) {
		this.riskEvaluates.add(riskEvaluate);
	}

	/**
	 * 风控评估自动填充开始结束时间
	 * @author yakir
	 * @date 2021/8/29 17:59
	 * @param evaluateContext
	 * @param evaluateReport
	 * @return boolean
	 */
	public boolean eval(EvaluateContext evaluateContext, EvaluateReport evaluateReport) {

		evaluateReport.setStartTime(LocalDateTime.now());
		log.info("风控评估开始执行,执行参数{}", evaluateContext);
		boolean result = doEval(evaluateContext, evaluateReport);
		evaluateReport.setEndTime(LocalDateTime.now());
		log.info("风控评估结束执行,执行参数{},执行结果{}", evaluateContext, evaluateReport);
		return result;

	}

	/**
	 * 风控评估
	 * @author yakir
	 * @date 2021/8/29 17:36
	 * @param evaluateContext 评估上下文
	 * @param evaluateReport 评估报告
	 * @return boolean true 评估通过 false 评估未通过
	 */
	private boolean doEval(EvaluateContext evaluateContext, EvaluateReport evaluateReport) {
		for (int i = 0; i < this.riskEvaluates.size(); i++) {
			RiskEvaluate riskEvaluate = this.riskEvaluates.get(i);
			if (!riskEvaluate.doEval(evaluateContext, evaluateReport)) {
				return false;
			}
			this.position = i;
		}
		return true;
	}

}

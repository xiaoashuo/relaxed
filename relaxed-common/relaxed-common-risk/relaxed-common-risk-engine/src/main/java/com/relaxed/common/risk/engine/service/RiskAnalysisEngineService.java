package com.relaxed.common.risk.engine.service;

import com.relaxed.common.model.result.R;

import java.util.Map;

/**
 * @author Yakir
 * @Topic RiskAnalysisEngineService
 * @Description
 * @date 2021/8/29 8:46
 * @Version 1.0
 */
public interface RiskAnalysisEngineService {

	/**
	 * 评估风险
	 * @author yakir
	 * @date 2021/8/29 8:48
	 * @param modelGuid
	 * @param reqId
	 * @param jsonInfo
	 * @return com.relaxed.common.model.result.R
	 */
	R evaluateRisk(String modelGuid, String reqId, Map jsonInfo);

}

package com.relaxed.common.risk.engine.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.relaxed.common.model.result.R;
import com.relaxed.common.risk.engine.enums.ModelEnums;
import com.relaxed.common.risk.engine.exception.RiskEngineException;
import com.relaxed.common.risk.engine.manage.FieldValidateService;
import com.relaxed.common.risk.engine.manage.ModelEventManageService;
import com.relaxed.common.risk.engine.manage.ModelManageService;
import com.relaxed.common.risk.engine.manage.PreItemManageService;
import com.relaxed.common.risk.engine.model.dto.RiskResultCode;
import com.relaxed.common.risk.engine.model.vo.ModelVO;
import com.relaxed.common.risk.engine.rules.EvaluateContext;
import com.relaxed.common.risk.engine.rules.EvaluateReport;
import com.relaxed.common.risk.engine.rules.RiskEvaluateChain;
import com.relaxed.common.risk.engine.service.RiskAnalysisEngineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Yakir
 * @Topic RiskAnalysisEngineService
 * @Description
 * @date 2021/8/29 8:46
 * @Version 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RiskAnalysisEngineServiceImpl implements RiskAnalysisEngineService {

	private final ModelManageService modelManageService;

	private final FieldValidateService fieldValidateService;

	private final PreItemManageService preItemManageService;

	private final ModelEventManageService modelEventManageService;

	private final RiskEvaluateChain riskEvaluateChain;

	@Value("${sys.conf.entity-duplicate-insert}")
	private String isDuplicate;

	@Override
	public R evaluateRisk(String modelGuid, String reqId, Map jsonInfo) {
		log.info("req info:{},{},{}", modelGuid, reqId, jsonInfo);
		ModelVO modelVO = modelManageService.getByGuid(modelGuid);
		// 1.基础规则效验
		if (modelVO == null) {
			R.failed(RiskResultCode.MODEL_NOT_EXISTS);
		}
		if (!ModelEnums.StatusEnum.ENABLE.getStatus().equals(modelVO.getStatus())) {
			return R.failed(RiskResultCode.MODEL_DISABLED);
		}

		if (ModelEnums.FieldValidEnum.validField(modelVO.getFieldValidate())) {
			// 验证字段
			Map<String, String> validateMap = fieldValidateService.validate(modelVO.getId(), jsonInfo);
			if (CollectionUtil.isNotEmpty(validateMap)) {
				return R.failed(RiskResultCode.FIELD_VALID_NOT_PASSED).setData(validateMap);
			}
		}
		try {
			// 2.预处理字段提取
			Map<String, Object> prepare = preItemManageService.prepare(modelVO.getId(), jsonInfo);
			// 3.保存model event
			modelEventManageService.save(modelVO.getId(), JSONUtil.toJsonStr(jsonInfo), JSONUtil.toJsonStr(prepare),
					isAllowDulicate());
			// 4.执行分析
			EvaluateContext evaluateContext = new EvaluateContext();
			evaluateContext.setReqId(reqId);
			evaluateContext.setModelVo(modelVO);
			evaluateContext.setEventJson(jsonInfo);
			evaluateContext.setPreItemMap(prepare);
			// 执行报告
			EvaluateReport evaluateReport = new EvaluateReport();
			riskEvaluateChain.eval(evaluateContext, evaluateReport);

			// 5.for elastic analysis
		}
		catch (Exception e) {
			log.error("process error", e);
			throw new RiskEngineException("数据处理异常:{}", e, e.getMessage());
		}
		// 6. 缓存分析结果
		// 7. 保存事件信息和分析结果用于后续分析
		return null;
	}

	private boolean isAllowDulicate() {
		boolean isAllowDuplicate = false;
		if (isDuplicate != null && isDuplicate.equals("true")) {
			isAllowDuplicate = true;
		}
		return isAllowDuplicate;
	}

}
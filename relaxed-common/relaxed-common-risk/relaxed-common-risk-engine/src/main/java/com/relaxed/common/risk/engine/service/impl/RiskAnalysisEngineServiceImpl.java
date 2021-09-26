package com.relaxed.common.risk.engine.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.relaxed.common.cache.CacheOperator;
import com.relaxed.common.model.result.R;
import com.relaxed.common.risk.engine.config.EngineProperties;
import com.relaxed.common.risk.biz.distributor.risk.RiskEvalAsyncDistributor;
import com.relaxed.common.risk.engine.core.handler.RiskReportHandler;
import com.relaxed.common.risk.engine.exception.RiskEngineException;
import com.relaxed.common.risk.engine.service.*;
import com.relaxed.common.risk.model.dto.RiskResultCode;
import com.relaxed.common.risk.model.enums.ModelEnums;
import com.relaxed.common.risk.model.vo.ModelVO;
import com.relaxed.common.risk.engine.rules.EvaluateContext;
import com.relaxed.common.risk.engine.rules.EvaluateReport;
import com.relaxed.common.risk.engine.rules.RiskEvaluateChain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

	private final CacheOperator<String> cacheManage;

	private final RiskReportHandler riskReportHandler;

	private final RiskEvalAsyncDistributor riskEvalAsyncDistributor;

	@Override
	public R evaluateRisk(String modelGuid, String reqId, Map jsonInfo) {
		log.info("req info:{},{},{}", modelGuid, reqId, jsonInfo);
		ModelVO modelVO = modelManageService.getByGuid(modelGuid);
		// 1.基础规则效验
		if (modelVO == null) {
			return R.failed(RiskResultCode.MODEL_NOT_EXISTS);
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
		// 执行报告
		EvaluateReport evaluateReport = new EvaluateReport();
		try {
			// 2.预处理字段提取
			Map<String, Object> prepare = preItemManageService.prepare(modelVO.getId(), jsonInfo);
			// 3.保存model event
			modelEventManageService.save(modelVO, JSONUtil.toJsonStr(jsonInfo), JSONUtil.toJsonStr(prepare),
					EngineProperties.getDuplicate());
			// 4.执行分析
			EvaluateContext evaluateContext = new EvaluateContext();
			evaluateContext.setReqId(reqId);
			evaluateContext.setModelVo(modelVO);
			evaluateContext.setEventJson(jsonInfo);
			evaluateContext.setPreItemMap(prepare);
			boolean result = riskEvaluateChain.eval(evaluateContext, evaluateReport);
			if (!result) {
				return R.failed(RiskResultCode.RISK_EVAL_NOT_PASSED).setMessage(evaluateReport.getErrorMsg())
						.setData(evaluateReport);
			}
			// 5.for elastic analysis
			Long eventTimeMillis = (Long) jsonInfo.get(modelVO.getReferenceDate());
			String timeStr = DateUtil.format(new Date(eventTimeMillis), "yyyy-MM-dd'T'HH:mm:ssZ");
			prepare.put("risk_ref_datetime", timeStr);
		}
		catch (Exception e) {
			log.error("process error", e);
			throw new RiskEngineException("数据处理异常", e);
		}
		// 6. 缓存分析结果
		String jsonReport = JSONUtil.toJsonStr(evaluateReport);
		cacheManage.set(buildReportCacheKey(modelGuid, reqId), jsonReport, 5 * 60, TimeUnit.SECONDS);
		// 7. 保存事件信息和分析结果用于后续分析 可以发送到es redis 等等
		riskReportHandler.handle(modelGuid, reqId, jsonReport);
		return R.ok(evaluateReport);
	}

	@Override
	public R evaluateRiskAsync(String modelGuid, String reqId, Map jsonInfo) {
		riskEvalAsyncDistributor.distribute(modelGuid, reqId, jsonInfo);
		// 直接返回受理成功 实际执行子任务去执行评估
		return R.ok();
	}

	@Override
	public R evaluateReport(String modelGuid, String reqId) {
		String key = buildReportCacheKey(modelGuid, reqId);
		// 风控报告
		String jsonResult = cacheManage.get(key);
		if (StrUtil.isEmpty(jsonResult)) {
			return R.failed(RiskResultCode.RISK_EVAL_EXPIRED);
		}
		return R.ok(JSONUtil.toBean(jsonResult, EvaluateReport.class));
	}

	private String buildReportCacheKey(String modelGuid, String reqId) {
		return "engine_" + modelGuid + reqId;
	}

}

package com.relaxed.common.risk.engine.rules.extractor;

import com.relaxed.common.risk.model.enums.FieldType;
import com.relaxed.common.risk.model.vo.FieldVO;
import com.relaxed.common.risk.engine.rules.EvaluateContext;
import com.relaxed.common.risk.engine.rules.EvaluateReport;
import com.relaxed.common.risk.engine.rules.executor.AbstractionRiskEvaluate;

import java.util.List;
import java.util.Map;

/**
 * @author Yakir
 * @Topic DefaultFieldExtractor
 * @Description
 * @date 2021/8/30 13:35
 * @Version 1.0
 */

public class SimpleFieldExtractor implements FieldExtractor {

	@Override
	public String extractorFieldName(String originText) {
		return originText.replace("fields.", "").replace("preItems.", "");
	}

	@Override
	public <T> T extractorFieldValue(String fieldName, Map... maps) {
		for (Map map : maps) {
			T searchFieldVal = (T) map.get(fieldName);
			if (searchFieldVal != null) {
				return searchFieldVal;
			}
		}
		return null;
	}

	@Override
	public <T> T extractorFieldValue(String fieldName, EvaluateContext evaluateContext, EvaluateReport evaluateReport) {
		Map eventJson = evaluateContext.getEventJson();
		Map<String, Object> preItemMap = evaluateContext.getPreItemMap();
		// 抽象特征
		Map<String, ?> abstractionMAP = evaluateReport.getEvaluateData().get(AbstractionRiskEvaluate.ABSTRACTIONS);

		return extractorFieldValue(fieldName, eventJson, preItemMap, abstractionMAP);
	}

	@Override
	public FieldType extractorFieldType(String fieldName, List<FieldVO> fieldVOs) {
		for (FieldVO fieldVO : fieldVOs) {
			if (fieldVO.getFieldName().equals(fieldName)) {
				return FieldType.valueOf(fieldVO.getFieldType());
			}
		}
		return null;
	}

}

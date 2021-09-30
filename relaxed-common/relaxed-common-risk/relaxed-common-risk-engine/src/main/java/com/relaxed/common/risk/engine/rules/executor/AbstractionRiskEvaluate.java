package com.relaxed.common.risk.engine.rules.executor;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

import com.relaxed.common.risk.engine.service.AbstractionManageService;
import com.relaxed.common.risk.engine.service.DataListManageService;
import com.relaxed.common.risk.engine.service.FieldManageService;
import com.relaxed.common.risk.model.entity.FieldMeta;
import com.relaxed.common.risk.model.enums.AbstractionEnum;
import com.relaxed.common.risk.model.enums.FieldType;
import com.relaxed.common.risk.model.vo.AbstractionVO;
import com.relaxed.common.risk.model.vo.FieldVO;
import com.relaxed.common.risk.model.vo.ModelVO;
import com.relaxed.common.risk.engine.rules.AbstractRiskEvaluate;
import com.relaxed.common.risk.engine.rules.EvaluateContext;
import com.relaxed.common.risk.engine.rules.EvaluateReport;
import com.relaxed.common.risk.engine.rules.extractor.FieldExtractor;
import com.relaxed.common.risk.engine.rules.statistics.AggregateInvoker;
import com.relaxed.common.risk.engine.rules.statistics.domain.AggregateParamBO;
import com.relaxed.common.risk.engine.rules.statistics.domain.AggregateResult;
import com.relaxed.common.risk.engine.rules.statistics.enums.IAggregateFunction;
import com.relaxed.common.risk.engine.rules.statistics.provider.AggregateFunctionProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yakir
 * @Topic AbstractionRiskEvaluate
 * @Description 特征提取评估
 * @date 2021/8/29 17:45
 * @Version 1.0
 */
@Order(1)
@Component
@RequiredArgsConstructor
@Slf4j
public class AbstractionRiskEvaluate extends AbstractRiskEvaluate {

	/**
	 * 阶段标识
	 */
	public static final String ABSTRACTIONS = "abstractions";

	private final AbstractionManageService abstractionManageService;

	private final FieldManageService fieldManageService;

	private final FieldExtractor fieldExtractor;

	private final AggregateFunctionProvider aggregateFunctionProvider;

	private final AggregateInvoker aggregateInvoker;

	private final DataListManageService dataListManageService;

	@Override
	public String getName() {
		return ABSTRACTIONS;
	}

	@Override
	public boolean doEval(EvaluateContext evaluateContext, EvaluateReport evaluateReport) {
		// 存放当前评估器产生的数据信息
		Map<String, Object> extParam = new HashMap<>();
		ModelVO modelVo = evaluateContext.getModelVo();
		// 1.list abstraction
		List<AbstractionVO> abstractionVOS = abstractionManageService.getByModelId(modelVo.getId());
		if (CollectionUtil.isEmpty(abstractionVOS)) {
			// 若特征为空 等同步步骤评估成功
			log.info("模型{}特征为空", modelVo.getId());
			evaluateReport.putEvaluateMap(ABSTRACTIONS, extParam);
			return true;
		}
		// 获取日期字段名称
		String refDateFieldName = modelVo.getReferenceDate();
		// 获取日期字段值 时间
		Long refDateTimestamp = fieldExtractor.extractorFieldValue(refDateFieldName, evaluateContext, evaluateReport);
		Date refDate = refDateTimestamp == null ? null : new Date(refDateTimestamp);
		if (refDate == null) {
			evaluateReport.setErrorMsg("search {} field value not exists.", refDateFieldName);
			return false;
		}
		// 2.获取预加载的黑/白名单集合
		Map<String, Object> blackWhiteMap = dataListManageService.getDataListMap(modelVo.getId());
		// 3. 加载所有的字段信息 基础字段+预处理字段
		Map<String, String> fieldTypeMap = fieldManageService.getFieldTypeMap(modelVo.getId());
		// 3. 按 script 的条件， 分别统计 abstraction
		for (AbstractionVO abstractionVO : abstractionVOS) {
			if (!AbstractionEnum.StatusEnum.ENABLE.getStatus().equals(abstractionVO.getStatus())) {
				continue;
			}
			// 规则脚本
			String ruleScript = abstractionVO.getRuleScript();
			// 规则脚本入口
			String ruleScriptEntry = abstractionVO.getRuleScriptEntry();
			boolean match = checkScript(ruleScript, ruleScriptEntry, evaluateContext, blackWhiteMap);
			if (!match) {
				// 脚本规则不匹配 则过滤此特征提取
				extParam.put(abstractionVO.getName(), -1);
				continue;
			}
			// 查询间隔类型 日 月 周 年等等
			Integer searchIntervalType = abstractionVO.getSearchIntervalType();
			// 查询间隔值
			Integer searchIntervalValue = abstractionVO.getSearchIntervalValue();
			// 查询字段名称
			String searchFieldName = fieldExtractor.extractorFieldName(abstractionVO.getSearchField());
			// 统计指标开始时间
			Date beginDate = DateUtil.offset(refDate, DateField.of(searchIntervalType), searchIntervalValue * -1)
					.toJdkDate();
			// 查询字段值
			Object searchFieldVal = fieldExtractor.extractorFieldValue(searchFieldName, evaluateContext,
					evaluateReport);
			if (searchFieldVal == null) {
				evaluateReport.setErrorMsg("search {} field value not exists.", searchFieldName);
				return false;
			}
			// 聚合特征提取信息集
			AggregateParamBO aggregateBo = new AggregateParamBO();
			aggregateBo.setModelId(modelVo.getId());
			aggregateBo.setBeginDate(beginDate);
			// 指向日期字段元数据
			FieldMeta refDateFieldMeta = new FieldMeta(refDateFieldName, refDate,
					fieldExtractor.extractorFieldType(refDateFieldName, fieldTypeMap));
			aggregateBo.setRefDateFieldMeta(refDateFieldMeta);
			// 查询字段元数据
			FieldMeta searchFieldMeta = new FieldMeta(searchFieldName, searchFieldVal,
					fieldExtractor.extractorFieldType(searchFieldName, fieldTypeMap));
			aggregateBo.setSearchFieldMeta(searchFieldMeta);
			// 查询函数字段名称
			String functionFieldName = fieldExtractor.extractorFieldName(abstractionVO.getFunctionField());
			if (StrUtil.isNotEmpty(functionFieldName)) {
				// 函数字段值
				Object functionFieldVal = fieldExtractor.extractorFieldValue(functionFieldName, evaluateContext,
						evaluateReport);
				// 函数字段处理
				FieldType fieldType = fieldExtractor.extractorFieldType(functionFieldName, fieldTypeMap);
				if (fieldType == null) {
					evaluateReport.setErrorMsg("search function {} field type not exists.", functionFieldName);
					return false;
				}
				FieldMeta fieldMeta = new FieldMeta(functionFieldName, functionFieldVal, fieldType);
				aggregateBo.setFunctionFieldMeta(fieldMeta);
			}
			// 执行聚合操作 by 聚合类型 平均值 最大值等等
			Integer aggregateType = abstractionVO.getAggregateType();
			IAggregateFunction aggregateFunction = aggregateFunctionProvider.provide(aggregateType);
			AggregateResult aggregateResult = aggregateInvoker.invoke(aggregateFunction,
					aggregateInvoker.buildContext(evaluateContext, aggregateBo));
			Object executeResult = aggregateResult.getExecuteResult();
			extParam.put(abstractionVO.getName(), executeResult);
		}
		evaluateReport.putEvaluateMap(getName(), extParam);
		return true;
	}

}

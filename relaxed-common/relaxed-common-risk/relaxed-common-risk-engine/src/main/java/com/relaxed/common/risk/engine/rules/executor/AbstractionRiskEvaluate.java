package com.relaxed.common.risk.engine.rules.executor;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.relaxed.common.risk.engine.enums.AbstractionEnum;
import com.relaxed.common.risk.engine.enums.FieldType;
import com.relaxed.common.risk.engine.manage.AbstractionManageService;
import com.relaxed.common.risk.engine.manage.DataListManageService;
import com.relaxed.common.risk.engine.manage.FieldManageService;
import com.relaxed.common.risk.engine.model.vo.AbstractionVO;
import com.relaxed.common.risk.engine.model.vo.FieldVO;
import com.relaxed.common.risk.engine.model.vo.ModelVO;
import com.relaxed.common.risk.engine.rules.AbstractRiskEvaluate;
import com.relaxed.common.risk.engine.rules.EvaluateContext;
import com.relaxed.common.risk.engine.rules.EvaluateReport;
import com.relaxed.common.risk.engine.rules.extractor.FieldExtractor;
import com.relaxed.common.risk.engine.rules.script.RuleScriptHandler;
import com.relaxed.common.risk.engine.rules.script.ScriptResult;
import com.relaxed.common.risk.engine.rules.statistics.AggregateInvoker;
import com.relaxed.common.risk.engine.rules.statistics.domain.AggregateParam;
import com.relaxed.common.risk.engine.rules.statistics.domain.AggregateResult;
import com.relaxed.common.risk.engine.rules.statistics.enums.AggregateFunction;
import com.relaxed.common.risk.engine.rules.statistics.provider.AggregateFunctionProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
@RequiredArgsConstructor
@Slf4j
public class AbstractionRiskEvaluate extends AbstractRiskEvaluate {

	/**
	 * 阶段标识
	 */
	public static final String ABSTRACTIONS = "abstractions";

	private final AbstractionManageService abstractionManageService;

	private final FieldManageService fieldManageService;

	private final RuleScriptHandler ruleScriptHandler;

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
		Map<String, Object> extParam = new HashMap<>();
		ModelVO modelVo = evaluateContext.getModelVo();
		Map eventJson = evaluateContext.getEventJson();
		Map<String, Object> preItemMap = evaluateContext.getPreItemMap();
		// 1.list abstraction
		List<AbstractionVO> abstractionVOS = abstractionManageService.getByModelId(modelVo.getId());
		if (CollectionUtil.isEmpty(abstractionVOS)) {
			// 若特征为空 等同步步骤评估成功
			extParam.put(ABSTRACTIONS, "模型特征为空");
			evaluateReport.putEvaluateMap(ABSTRACTIONS, extParam);
			return true;
		}
		// 2. 按 script 的条件， 分别统计 abstraction
		for (AbstractionVO abstractionVO : abstractionVOS) {
			if (!AbstractionEnum.StatusEnum.ENABLE.getStatus().equals(abstractionVO.getStatus())) {
				continue;
			}
			// 查询字段名称
			String searchFieldName = fieldExtractor.extractorFieldName(abstractionVO.getSearchField());
			// 聚合类型 平均值 最大值等等
			Integer aggregateType = abstractionVO.getAggregateType();
			// 查询间隔类型 日 月 周 年等等
			Integer searchIntervalType = abstractionVO.getSearchIntervalType();
			// 查询间隔值
			Integer searchIntervalValue = abstractionVO.getSearchIntervalValue();
			// 查询函数字段名称
			String functionFieldName = fieldExtractor.extractorFieldName(abstractionVO.getFunctionField());
			// 规则脚本
			String ruleScript = abstractionVO.getRuleScript();
			// 获取预加载的黑/白名单集合
			Map<String, Object> blackWhiteMap = dataListManageService.getDataListMap(modelVo.getId());
			boolean match = checkAbstractionScript(ruleScript, eventJson, blackWhiteMap);
			if (!match) {
				// 脚本检查不通过 过滤该特征
				extParam.put(abstractionVO.getName(), -1);
				continue;
			}
			// 获取日期字段名称
			String refDateFieldName = modelVo.getReferenceDate();
			// 获取事件指向时间
			Date refDate = convertRefDate(eventJson, refDateFieldName);
			if (refDate == null) {
				evaluateReport.setMsg("时间格式不正确");
				return false;
			}
			// 统计指标开始时间
			Date beginDate = DateUtil.offset(refDate, DateField.of(searchIntervalType), searchIntervalValue * -1)
					.toJdkDate();
			// 查询字段值
			Object searchFieldVal = fieldExtractor.extractorFieldValue(searchFieldName, extParam, preItemMap);
			if (searchFieldVal == null) {
				evaluateReport.setMsg("search {} field value not exists.", searchFieldName);
				return false;
			}
			// 函数字段值
			Object functionFieldVal = fieldExtractor.extractorFieldValue(functionFieldName, eventJson);
			// 函数字段处理
			FieldType fieldType = null;
			if (StrUtil.isNotEmpty(functionFieldName)) {
				List<FieldVO> fieldVos = fieldManageService.getFieldVos(modelVo.getId());
				// 函数字段
				fieldType = fieldExtractor.extractorFieldType(functionFieldName, fieldVos);
				if (fieldType == null) {
					// 因为预处理字段没有字段类型，暂时设置为String
					// TODO: 目前只有高级函数使用了 functionFieldType。
					fieldType = FieldType.valueOf("STRING");
				}
			}
			// 执行聚合操作 by 聚合类型
			AggregateFunction aggregateFunction = aggregateFunctionProvider.provide(aggregateType);
			// 构建参数BO
			AggregateParam aggregateBo = new AggregateParam().setModelId(modelVo.getId())
					.setSearchFieldName(searchFieldName).setSearchFieldVal(searchFieldVal)
					.setRefDateFieldName(refDateFieldName).setRefDateFieldVal(refDate).setBeginDate(beginDate)
					.setFunctionFieldName(functionFieldName).setFunctionFieldVal(functionFieldVal)
					.setFunctionFieldType(fieldType);
			AggregateResult aggregateResult = aggregateInvoker.invoke(aggregateFunction,
					aggregateInvoker.convert(aggregateBo));
			Object executeResult = aggregateResult.getExecuteResult();
			extParam.put(abstractionVO.getName(), executeResult);
		}
		evaluateReport.putEvaluateMap(ABSTRACTIONS, extParam);
		return true;
	}

	private Date convertRefDate(Map eventJson, String refDateFieldName) {
		Date date = null;
		try {
			Long refDateTimeMills = (Long) eventJson.get(refDateFieldName);
			date = new Date(refDateTimeMills);
		}
		catch (Exception e) {
			log.error("时间转换异常", e);
		}
		return date;
	}

	/**
	 * 检查特征脚本
	 * @author yakir
	 * @date 2021/8/30 12:54
	 * @param ruleScript
	 * @param eventJson
	 * @param blackWhiteMap
	 * @return boolean
	 */
	private boolean checkAbstractionScript(String ruleScript, Map eventJson, Map<String, Object> blackWhiteMap) {
		Object[] args = { eventJson, blackWhiteMap };
		Boolean ret = false;
		try {
			ScriptResult scriptResult = ruleScriptHandler
					.invokeMethod(ruleScriptHandler.buildContext(ruleScript, "check", args));
			ret = scriptResult.getRunResult();
		}
		catch (Exception e) {
			log.error("params:{},rule:{}", args, ruleScript, e);
		}
		return ret;
	}

}

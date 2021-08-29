package com.relaxed.common.risk.engine.rules.executor;

import cn.hutool.core.collection.CollectionUtil;
import com.relaxed.common.risk.engine.enums.AbstractionEnum;
import com.relaxed.common.risk.engine.manage.AbstractionManageService;
import com.relaxed.common.risk.engine.model.vo.AbstractionVO;
import com.relaxed.common.risk.engine.model.vo.ModelVO;
import com.relaxed.common.risk.engine.rules.AbstractRiskEvaluate;
import com.relaxed.common.risk.engine.rules.EvaluateContext;
import com.relaxed.common.risk.engine.rules.EvaluateReport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

	@Override
	public boolean doEval(EvaluateContext evaluateContext, EvaluateReport evaluateReport) {
		long start = System.currentTimeMillis();
		log.info("特征风险评估,开始时间{}", start);
		Map<String, Object> extParam = new HashMap<>();
		ModelVO modelVo = evaluateContext.getModelVo();
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

		}

		long end = System.currentTimeMillis();
		log.info("特征风险评估,结束时间{},耗时{} ms", end, end - start);
		return true;
	}

}

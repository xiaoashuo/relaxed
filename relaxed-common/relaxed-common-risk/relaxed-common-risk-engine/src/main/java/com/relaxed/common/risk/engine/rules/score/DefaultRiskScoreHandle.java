package com.relaxed.common.risk.engine.rules.score;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.relaxed.common.risk.model.vo.RuleVO;
import com.relaxed.common.risk.engine.rules.EvaluateContext;
import com.relaxed.common.risk.engine.rules.EvaluateReport;
import com.relaxed.common.risk.engine.rules.extractor.FieldExtractor;
import com.relaxed.common.risk.engine.utils.ScoreUtil;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Yakir
 * @Topic DefaultRiskScoreHandle
 * @Description
 * @date 2021/9/9 16:53
 * @Version 1.0
 */
@RequiredArgsConstructor
public class DefaultRiskScoreHandle implements RiskScoreHandler {

	private final FieldExtractor fieldExtractor;

	@Override
	public BigDecimal computeRule(EvaluateContext evaluateContext, EvaluateReport evaluateReport, RuleVO ruleVO) {
		// 初始得分
		BigDecimal initScore = new BigDecimal(ruleVO.getInitScore());
		BigDecimal extra = BigDecimal.ZERO;
		// 最大分数
		BigDecimal maxScore = new BigDecimal(ruleVO.getMax());
		// 特征字段名称 计算特征字段 计分 fields.userId preItem.userId, abstractions.userId
		String abstractionName = ruleVO.getAbstractionName();
		// TODO 优化 字段值提取 用spel
		if (StrUtil.isNotEmpty(abstractionName)) {
			// 后续增加比列
			BigDecimal rate = new BigDecimal(ruleVO.getRate() * 0.01);
			// 基数
			BigDecimal base = new BigDecimal(ruleVO.getBaseNum());
			if (abstractionName.indexOf(StrPool.DOT) != -1) {
				String[] varNames = StrUtil.splitToArray(abstractionName, StrPool.DOT);
				Object val = fieldExtractor.extractorFieldValue(varNames[1], evaluateContext, evaluateReport);
				if (val instanceof Number) {
					extra = NumberUtil.toBigDecimal((Number) val);
				}
				else {
					extra = NumberUtil.toBigDecimal(val.toString());
				}
				extra = extra.multiply(rate);
				// 操作方式 ADD SUB MUL DIV
				String operator = ruleVO.getOperator();
				extra = ScoreUtil.exec(operator, base, extra);
			}
		}
		BigDecimal score = initScore.add(extra);
		// 规则得分设置最大值. 若得分超出 最大分数 则同步为最大分数
		if (maxScore.compareTo(BigDecimal.ZERO) > 0 && score.compareTo(maxScore) > 0) {
			score = maxScore;
		}
		return score;
	}

}

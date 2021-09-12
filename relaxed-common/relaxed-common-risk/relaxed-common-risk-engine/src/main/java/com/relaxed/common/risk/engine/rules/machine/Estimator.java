package com.relaxed.common.risk.engine.rules.machine;

import com.relaxed.common.risk.model.vo.ModelConfVO;
import com.relaxed.common.risk.engine.rules.EvaluateContext;
import com.relaxed.common.risk.engine.rules.EvaluateReport;

import java.util.Map;

/**
 * @author Yakir
 * @Topic Estimator
 * @Description 估算器 *
 * <p>
 * * 机器学习模型执行器接口 *
 * </p>
 * *
 * <p>
 * * 该接口内置TensorFlow实现，目前版本只考虑输入层为离散值的情况，不考虑词嵌入和融入卷积层，其中 *
 * 离散值通过表达式取数从前置流程传递过来，模型的预测结果为事件评分。 *
 * </p>
 * *
 * <p>
 * * 模型抽象：y=f(x) *
 * </p>
 * @date 2021/8/31 9:45
 * @Version 1.0
 */
public interface Estimator {

	/**
	 * 线性回归模型
	 */
	String TYPE_REGRESSION = "REGRESSION";

	/**
	 * 预测 评分值
	 * @author yakir
	 * @date 2021/8/31 9:46
	 * @param modelConfVO
	 * @param evaluateContext
	 * @param evaluateReport
	 * @return float 事件评分。
	 */
	float predict(ModelConfVO modelConfVO, EvaluateContext evaluateContext, EvaluateReport evaluateReport);

	/**
	 * 获取评估器类型
	 * @author yakir
	 * @date 2021/8/31 9:46
	 * @return java.lang.String
	 */
	String getType();

}

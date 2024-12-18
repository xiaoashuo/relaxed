package com.relaxed.common.log.biz.service;

import com.relaxed.common.log.biz.model.LogBizInfo;
import com.relaxed.common.log.biz.spel.LogSpelEvaluationContext;

/**
 * @author Yakir
 * @Topic ILogBizEnhance
 * @Description 日志解析增强
 * @date 2023/12/21 10:15
 * @Version 1.0
 */
public interface ILogBizEnhance {

	/**
	 * 日志增强
	 * @param logBizInfo
	 * @param spelContext
	 * @return
	 */
	void enhance(LogBizInfo logBizInfo, LogSpelEvaluationContext spelContext);

}

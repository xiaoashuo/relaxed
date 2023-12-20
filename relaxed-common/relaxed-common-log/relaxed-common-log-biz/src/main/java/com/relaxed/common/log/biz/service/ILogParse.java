package com.relaxed.common.log.biz.service;

import com.relaxed.common.log.biz.annotation.BizLog;
import com.relaxed.common.log.biz.model.LogBizInfo;
import com.relaxed.common.log.biz.spel.LogSpelEvaluationContext;

import java.lang.reflect.Method;

/**
 * @author Yakir
 * @Topic ILogParseRule
 * @Description 解析器规则
 * @date 2023/12/18 16:09
 * @Version 1.0
 */
public interface ILogParse {

	/**
	 * 是否记录日志
	 * @param conditionSpel
	 * @return
	 */
	boolean isRecordLog(LogSpelEvaluationContext context, String conditionSpel);

	/**
	 * 构建spel解析上下文
	 * @param target
	 * @param method
	 * @param args
	 * @return
	 */
	LogSpelEvaluationContext buildContext(Object target, Method method, Object[] args);

	/**
	 * 前置业务解析器
	 * @param logSpelContext
	 * @param bizLog
	 * @return
	 */
	LogBizInfo beforeResolve(LogSpelEvaluationContext logSpelContext, BizLog bizLog);

	/**
	 * 后置参数解析
	 * @param logBizOp
	 * @param logSpelContext
	 * @param bizLog
	 * @return logBizOp
	 */
	LogBizInfo afterResolve(LogBizInfo logBizOp, LogSpelEvaluationContext logSpelContext, BizLog bizLog);

}

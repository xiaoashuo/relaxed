package com.relaxed.common.log.biz.service;

import com.relaxed.common.log.biz.model.LogBizInfo;
import com.relaxed.common.log.biz.spel.LogSpelEvaluationContext;

/**
 * 日志业务增强接口，用于扩展和增强业务日志的功能 该接口允许在日志记录过程中添加额外的业务数据和上下文信息 实现类可以基于 SpEL 表达式上下文来丰富日志内容
 *
 * @author Yakir
 */
public interface ILogBizEnhance {

	/**
	 * 增强业务日志信息 在日志记录过程中添加额外的业务数据和上下文信息
	 * @param logBizInfo 业务日志信息对象，将被增强的日志数据
	 * @param spelContext SpEL 表达式上下文，包含解析所需的所有变量
	 */
	void enhance(LogBizInfo logBizInfo, LogSpelEvaluationContext spelContext);

}

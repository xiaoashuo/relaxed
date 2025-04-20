package com.relaxed.common.log.biz.service;

import com.relaxed.common.log.biz.annotation.BizLog;
import com.relaxed.common.log.biz.model.LogBizInfo;
import com.relaxed.common.log.biz.spel.LogSpelEvaluationContext;

import java.lang.reflect.Method;

/**
 * 日志解析接口，用于解析和处理业务日志 该接口定义了日志解析的基本流程，包括条件判断、上下文构建、前置和后置处理等 实现该接口的类需要提供具体的解析逻辑
 *
 * @author Yakir
 */
public interface ILogParse {

	/**
	 * 判断是否需要记录日志 通过解析条件表达式来决定是否记录当前操作的日志
	 * @param context SpEL 表达式上下文，包含解析所需的所有变量
	 * @param conditionSpel 条件表达式，用于判断是否记录日志
	 * @return 如果需要记录日志返回 true，否则返回 false
	 */
	boolean isRecordLog(LogSpelEvaluationContext context, String conditionSpel);

	/**
	 * 构建 SpEL 表达式解析上下文 创建并初始化用于解析表达式的上下文环境
	 * @param target 目标对象，即被调用的方法所属的对象
	 * @param method 被调用的方法
	 * @param args 方法调用的参数数组
	 * @return 初始化好的 SpEL 表达式上下文
	 */
	LogSpelEvaluationContext buildContext(Object target, Method method, Object[] args);

	/**
	 * 前置业务解析 在方法执行前进行日志信息的预处理
	 * @param logSpelContext SpEL 表达式上下文
	 * @param bizLog 业务日志注解，包含日志配置信息
	 * @return 预处理后的业务日志信息
	 */
	LogBizInfo beforeResolve(LogSpelEvaluationContext logSpelContext, BizLog bizLog);

	/**
	 * 后置参数解析 在方法执行后进行日志信息的后处理
	 * @param logBizOp 业务日志信息对象
	 * @param logSpelContext SpEL 表达式上下文
	 * @param bizLog 业务日志注解，包含日志配置信息
	 * @return 处理后的业务日志信息
	 */
	LogBizInfo afterResolve(LogBizInfo logBizOp, LogSpelEvaluationContext logSpelContext, BizLog bizLog);

}

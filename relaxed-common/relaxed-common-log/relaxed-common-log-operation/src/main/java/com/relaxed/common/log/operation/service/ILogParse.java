package com.relaxed.common.log.operation.service;

import com.relaxed.common.log.operation.annotation.BizLog;
import com.relaxed.common.log.operation.model.LogBizInfo;

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
     * 前置业务解析器
     * @param target
     * @param method
     * @param args
     * @param bizLog
     * @return
     */
    LogBizInfo beforeResolve(Object target, Method method, Object[] args, BizLog bizLog);

    /**
     * 后置参数解析
     * @param logBizOp
     * @param target
     * @param method
     * @param args
     * @param bizLog
     * @return logBizOp
     */
    LogBizInfo afterResolve(LogBizInfo logBizOp, Object target, Method method, Object[] args, BizLog bizLog);
}

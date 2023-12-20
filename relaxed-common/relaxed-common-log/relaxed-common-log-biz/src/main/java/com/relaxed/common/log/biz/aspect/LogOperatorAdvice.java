package com.relaxed.common.log.biz.aspect;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.StrUtil;

import com.relaxed.common.log.biz.annotation.BizLog;
import com.relaxed.common.log.biz.constant.LogRecordConstants;
import com.relaxed.common.log.biz.context.LogRecordContext;
import com.relaxed.common.log.biz.model.LogBizInfo;
import com.relaxed.common.log.biz.service.ILogParse;
import com.relaxed.common.log.biz.service.ILogRecordService;
import com.relaxed.common.log.biz.spel.LogSpelEvaluationContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @author Yakir
 * @Topic LogOperatorAdvice
 * @Description
 * @date 2023/12/14 11:20
 * @Version 1.0
 */
@RequiredArgsConstructor
@Slf4j
public class LogOperatorAdvice implements MethodInterceptor {

	private final ILogParse logParse;

	private final ILogRecordService logRecordService;

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();
		// 记录日志
		return execute(invocation, invocation.getThis(), method, invocation.getArguments());
	}

	private Object execute(MethodInvocation invoker, Object target, Method method, Object[] args) throws Throwable {
		// 放入一个空标签,存储当前方法临时参数
		LogRecordContext.putEmptySpan();
		// 获取操作日志注解
		BizLog bizLog = AnnotationUtil.getAnnotation(method, BizLog.class);

		// spel解析 业务日志注解
		LogSpelEvaluationContext logSpelContext = logParse.buildContext(target, method, args);
		boolean isRecordLog = logParse.isRecordLog(logSpelContext, bizLog.condition());
		// 不需要记录日志 直接执行方法
		if (!isRecordLog) {
			try {
				return invoker.proceed();
			}
			finally {
				LogRecordContext.poll();
			}
		}

		LogBizInfo logBizOp = logParse.beforeResolve(logSpelContext, bizLog);
		LogRecordContext.push(LogRecordConstants.S_TIME, System.currentTimeMillis());
		Object result;
		try {
			result = invoker.proceed();
			// 记录当前执行result
			LogRecordContext.push(LogRecordConstants.RESULT, result);
		}
		catch (Throwable throwable) {
			LogRecordContext.push(LogRecordConstants.ERR_MSG, StrUtil.maxLength(throwable.getMessage(), 200));
			// 这里要把目标方法的结果抛出来，不然会吞掉异常
			throw throwable;
		}
		finally {
			try {
				LogRecordContext.push(LogRecordConstants.E_TIME, System.currentTimeMillis());
				logBizOp = logParse.afterResolve(logBizOp, logSpelContext, bizLog);
				// 记录业务日志
				logRecordService.record(logBizOp);
			}
			finally {
				LogRecordContext.poll();
			}

		}

		return result;
	}

}

package com.relaxed.common.log.operation.aspect;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.relaxed.common.log.operation.annotation.BizLog;
import com.relaxed.common.log.operation.constants.LogRecordConstants;
import com.relaxed.common.log.operation.context.LogOperatorContext;
import com.relaxed.common.log.operation.model.LogBizInfo;
import com.relaxed.common.log.operation.service.ILogParse;
import com.relaxed.common.log.operation.service.ILogRecordService;
import com.relaxed.common.log.operation.spel.LogSpelEvaluationContext;
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
		LogOperatorContext.putEmptySpan();
		//获取操作日志注解
		BizLog bizLog = AnnotationUtil.getAnnotation(method, BizLog.class);

	    //spel解析 业务日志注解
		LogSpelEvaluationContext logSpelContext = logParse.buildContext(target, method, args);
		boolean isRecordLog = logParse.isRecordLog(logSpelContext, bizLog.condition());
		//不需要记录日志 直接执行方法
		if (!isRecordLog){
			try {
				return invoker.proceed();
			}finally {
				LogOperatorContext.poll();
			}
		}

		LogBizInfo logBizOp= logParse.beforeResolve(logSpelContext,bizLog);
		//记录类名 方法名
		logBizOp.setClassName(target.getClass().getName());
		logBizOp.setMethodName(method.getName());
		logBizOp.setStartTime(System.currentTimeMillis());
		Object result;
		try {
			result=invoker.proceed();
			//记录当前执行result
			LogOperatorContext.push(LogRecordConstants.RESULT,result);
			logBizOp.setSuccess(true);
			logBizOp.setResult(JSONUtil.toJsonStr(result));
		}catch (Throwable e){
			logBizOp.setSuccess(false);
			logBizOp.setThrowable(e);
			LogOperatorContext.push(LogRecordConstants.ERR_MSG,StrUtil.maxLength(e.getMessage(),200));
			// 这里要把目标方法的结果抛出来，不然会吞掉异常
			throw e;
		}finally {
			try {
				logBizOp=logParse.afterResolve(logBizOp,logSpelContext,bizLog);
				logBizOp.setEndTime(System.currentTimeMillis());
				//记录业务日志
				logRecordService.record(logBizOp);
			} finally {
				LogOperatorContext.poll();
			}

		}

		return result;
	}











}

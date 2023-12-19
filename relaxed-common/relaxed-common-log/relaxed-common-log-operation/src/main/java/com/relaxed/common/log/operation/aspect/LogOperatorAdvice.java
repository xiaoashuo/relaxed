package com.relaxed.common.log.operation.aspect;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.relaxed.common.log.operation.annotation.BizLog;
import com.relaxed.common.log.operation.constants.LogRecordConstants;
import com.relaxed.common.log.operation.model.LogBizInfo;
import com.relaxed.common.log.operation.model.LogBizOp;
import com.relaxed.common.log.operation.service.ILogParse;
import com.relaxed.common.log.operation.service.ILogRecordService;
import com.relaxed.common.log.operation.spel.LogSpelEvaluationContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

		//获取操作日志注解
		BizLog bizLog = AnnotationUtil.getAnnotation(method, BizLog.class);

	    //spel解析 业务日志注解
		LogSpelEvaluationContext logSpelContext = logParse.buildContext(target, method, args);
		boolean isRecordLog = logParse.isRecordLog(logSpelContext, bizLog.condition());
		//不需要记录日志 直接执行方法
		if (!isRecordLog){
			return invoker.proceed();
		}
		// 放入一个空标签,存储当前方法临时参数
		LogOperatorContext.putEmptySpan();
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



	/**
	 * 获取不为空的待解析模板
	 * 从这个List里面我们也可以知道，哪些参数需要符合SpEl表达式
	 *
	 * @param logBizOps
	 * @return
	 */

	private List<String> getExpressTemplate(List<LogBizOp> logBizOps) {
		Set<String> set = new HashSet<>();
		for (LogBizOp logBizOp : logBizOps) {
			set.addAll(Arrays.asList(logBizOp.getBizNo(), logBizOp.getDetails(),
					logBizOp.getOperator(), logBizOp.getSuccess(), logBizOp.getFail(),
					logBizOp.getCondition()));
		}
		return set.stream().filter(s -> !ObjectUtils.isEmpty(s)).collect(Collectors.toList());
	}



	private void recordExecute(Object ret, Method method, Object[] args, Collection<LogOperatorOps> operations,
			Class<?> targetClass, boolean success, String msg, Map<String, String> functionNameAndReturnMap) {

	}

	private List<String> getBeforeExecuteFunctionTemplate(Collection<LogOperatorOps> operations) {
		return null;
	}

	private Map<String, String> processBeforeExecuteFunctionTemplate(List<String> spElTemplates, Class<?> targetClass,
			Method method, Object[] args) {
		return null;
	}

}

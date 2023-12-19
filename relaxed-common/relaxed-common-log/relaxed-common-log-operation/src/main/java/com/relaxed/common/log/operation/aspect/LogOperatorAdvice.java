package com.relaxed.common.log.operation.aspect;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.relaxed.common.log.operation.annotation.BizLog;
import com.relaxed.common.log.operation.constants.LogRecordConstants;
import com.relaxed.common.log.operation.model.LogBizInfo;
import com.relaxed.common.log.operation.model.LogBizOp;
import com.relaxed.common.log.operation.model.MethodExecResult;
import com.relaxed.common.log.operation.service.ILogParse;
import com.relaxed.common.log.operation.service.ILogRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
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
		// 放入一个空标签,存储当前方法临时参数
		LogOperatorContext.putEmptySpan();
		// 获取目标类
		Class<?> targetClass = ClassUtil.getClass(target);
		//获取操作日志注解
	//	BizLog[] bizLogs = method.getAnnotationsByType(BizLog.class);
		BizLog bizLog = AnnotationUtil.getAnnotation(method, BizLog.class);
	    //spel解析 业务日志注解
		LogBizInfo logBizOp= logParse.beforeResolve(target,method,args,bizLog);
		Object result = null;
		MethodExecResult executeResult = new MethodExecResult(true);
		try {
			result=invoker.proceed();
			//若业务日志为空,当条条件不需要记录日志
			if (logBizOp==null){
				return result;
			}
			//需要记录日志
			//记录当前执行result
			LogOperatorContext.push(LogRecordConstants.RESULT,result);
			logBizOp.setSuccess(true);
			logBizOp.setResult(JSONUtil.toJsonStr(result));
		}catch (Throwable e){
			logBizOp.setSuccess(false);
			//暂存异常
			executeResult.exception(e);
			logBizOp.setThrowable(e);
			// 这里要把目标方法的结果抛出来，不然会吞掉异常
			if (!executeResult.isSuccess()) {
				throw executeResult.getThrowable();
			}
		}finally {
			logBizOp=logParse.afterResolve(logBizOp,target,method,args,bizLog);
			logRecordService.record(logBizOp);
			LogOperatorContext.poll();
		}

		return result;
	}

//	private Object execute(MethodInvocation invoker, Object target, Method method, Object[] args) throws Throwable {
//		// 放入一个空标签,存储当前方法临时参数
//		LogOperatorContext.putEmptySpan();
//		// 获取目标类
//		Class<?> targetClass = ClassUtil.getClass(target);
//		//获取操作日志注解
//		//	BizLog[] bizLogs = method.getAnnotationsByType(BizLog.class);
//		BizLog bizLog = AnnotationUtil.getAnnotation(method, BizLog.class);
//		//spel解析 业务日志注解
//		LogBizInfo logBizOp= logParse.beforeResolve(target,method,args,bizLog);
//
//		//获取所有表达式模板
//		List<String> expressTemplate= getExpressTemplate(null);
//		//获取前置自定义函数值 函数名->函数值
//		//Map<String, Object> beforeFuncResultMap = logSpelParser.processBeforeExec(expressTemplate, method, args, targetClass);
//		Map<String, String> beforeFuncResultMap =new HashMap<>();
//		Object result = null;
//		MethodExecResult executeResult = new MethodExecResult(true);
//		try {
//			result=invoker.proceed();
//
//		}catch (Throwable e){
//			//暂存异常
//			executeResult.exception(e);
//			//	boolean existsNoFailTemp = bizLogOpsList.stream().anyMatch(bizLogOps -> ObjectUtils.isEmpty(bizLogOps.getFail()));
//			boolean existsNoFailTemp =  StrUtil.isEmpty(logBizOp.getErrorMsg());
//			if (!executeResult.isSuccess() && existsNoFailTemp) {
//				log.warn("[{}] 方法执行失败，@BizLog注解中 失败模板没有配置", method.getName());
//			} else {
//				// 解析SpEl表达式
//				Map<String, String> templateMap = logSpelParser.processAfterExec(expressTemplate, beforeFuncResultMap, method, args, targetClass, executeResult.getErrMsg(), result);
//				// 发送日志
//				sendLog(logBizOp, result, executeResult, templateMap);
//			}
//			// 这里要把目标方法的结果抛出来，不然会吞掉异常
//			if (!executeResult.isSuccess()) {
//				throw executeResult.getThrowable();
//			}
//		}finally {
//			LogOperatorContext.poll();
//		}
//
//		return result;
//	}
	/**
	 * 发送日志
	 *
	 * @param logBizOp
	 * @param result
	 * @param executeResult
	 * @param templateMap
	 */
	private void sendLog(LogBizInfo logBizOp, Object result, MethodExecResult executeResult, Map<String, String> templateMap) {
//		List<BizLogInfo> bizLogInfos = createBizLogInfo(templateMap, bizLogOps, executeResult);
//		if (!CollectionUtils.isEmpty(bizLogInfos)) {
//			bizLogInfos.forEach(bizLogInfo -> {
//				bizLogInfo.setResult(JSON.toJSONString(result));
//				// 发送日志（这里其实可以追加参数，判断是否多线程执行，目前是交由子类判断）
//				logRecordService.record(bizLogInfo);
//			});
//		}
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

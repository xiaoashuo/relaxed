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
 * 业务日志操作拦截器，负责处理被 {@link BizLog} 注解标记的方法。 该拦截器实现了方法的环绕通知，在方法执行前后进行日志记录： 1.
 * 执行前：解析注解配置，准备日志上下文 2. 执行中：记录方法执行时间、返回结果或异常信息 3. 执行后：完成日志解析并通过日志服务记录
 *
 * @author Yakir
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Slf4j
public class LogOperatorAdvice implements MethodInterceptor {

	/**
	 * 日志解析器，用于解析日志注解和处理日志内容
	 */
	private final ILogParse logParse;

	/**
	 * 日志记录服务，负责实际的日志存储操作
	 */
	private final ILogRecordService logRecordService;

	/**
	 * 拦截并处理方法调用。 实现 MethodInterceptor 接口的方法，负责拦截被 {@link BizLog} 注解标记的方法。
	 * @param invocation 方法调用对象，包含被拦截方法的相关信息
	 * @return 方法执行的结果
	 * @throws Throwable 执行过程中可能抛出的异常
	 */
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();
		return execute(invocation, invocation.getThis(), method, invocation.getArguments());
	}

	/**
	 * 执行方法并处理日志记录。 该方法完成以下任务： 1. 创建日志上下文 2. 解析日志注解配置 3. 判断是否需要记录日志 4. 执行目标方法 5.
	 * 收集执行结果和异常信息 6. 完成日志记录
	 * @param invoker 方法调用对象
	 * @param target 目标对象
	 * @param method 被调用的方法
	 * @param args 方法参数
	 * @return 方法执行的结果
	 * @throws Throwable 执行过程中可能抛出的异常
	 */
	private Object execute(MethodInvocation invoker, Object target, Method method, Object[] args) throws Throwable {
		// 创建日志上下文
		LogRecordContext.putEmptySpan();
		// 获取业务日志注解
		BizLog bizLog = AnnotationUtil.getAnnotation(method, BizLog.class);

		// 解析 SpEL 表达式并判断是否需要记录日志
		LogSpelEvaluationContext logSpelContext = null;
		boolean isRecordLog = false;
		try {
			logSpelContext = logParse.buildContext(target, method, args);
			isRecordLog = logParse.isRecordLog(logSpelContext, bizLog.condition());
		}
		catch (Throwable throwable) {
			log.error("解析日志条件表达式时发生异常", throwable);
		}
		// 不需要记录日志时直接执行方法
		if (!isRecordLog) {
			try {
				return invoker.proceed();
			}
			finally {
				LogRecordContext.poll();
			}
		}

		// 解析前置日志信息
		LogBizInfo logBizOp = null;
		try {
			logBizOp = logParse.beforeResolve(logSpelContext, bizLog);
		}
		catch (Throwable throwable) {
			log.error("解析前置日志信息时发生异常", throwable);
		}

		// 记录开始时间
		LogRecordContext.push(LogRecordConstants.S_TIME, System.currentTimeMillis());
		Object result;
		try {
			result = invoker.proceed();
			// 记录执行结果
			LogRecordContext.push(LogRecordConstants.RESULT, result);
		}
		catch (Throwable throwable) {
			// 记录异常信息，限制长度为200
			LogRecordContext.push(LogRecordConstants.ERR_MSG, StrUtil.maxLength(throwable.getMessage(), 200));
			throw throwable;
		}
		finally {
			try {
				// 记录结束时间
				LogRecordContext.push(LogRecordConstants.E_TIME, System.currentTimeMillis());
				// 解析后置日志信息
				logBizOp = logParse.afterResolve(logBizOp, logSpelContext, bizLog);
				// 记录业务日志
				logRecordService.record(logBizOp);
			}
			catch (Throwable throwable) {
				log.error("解析后置日志信息时发生异常", throwable);
			}
			finally {
				LogRecordContext.poll();
			}
		}

		return result;
	}

}

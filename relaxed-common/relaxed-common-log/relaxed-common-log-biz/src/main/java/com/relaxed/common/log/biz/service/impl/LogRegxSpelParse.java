package com.relaxed.common.log.biz.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import com.relaxed.common.log.biz.annotation.BizLog;
import com.relaxed.common.log.biz.constant.LogRecordConstants;
import com.relaxed.common.log.biz.context.LogOperatorContext;
import com.relaxed.common.log.biz.discover.LogRecordFuncDiscover;
import com.relaxed.common.log.biz.function.FuncEval;
import com.relaxed.common.log.biz.model.LogBizInfo;
import com.relaxed.common.log.biz.service.ILogParse;
import com.relaxed.common.log.biz.service.IOperatorGetService;
import com.relaxed.common.log.biz.spel.LogSeplUtil;
import com.relaxed.common.log.biz.spel.LogSpelEvaluationContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.env.Environment;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Yakir
 * @Topic LogRegxSpelParse
 * @Description
 * @date 2023/12/19 14:26
 * @Version 1.0
 */
@Slf4j
public class LogRegxSpelParse implements ILogParse, BeanFactoryAware, ApplicationContextAware {

	/**
	 * 这个正则表达式的含义为： 匹配一个包含在花括号中的字符串，其中花括号中可以包含任意数量的空白字符（包括空格、制表符、换行符等），
	 * 并且花括号中至少包含一个单词字符（字母、数字或下划线）。 =================================================
	 * 具体来说，该正则表达式由两部分组成：
	 * {s*(\w*)\s*}：表示匹配一个左花括号，后面跟随零个或多个空白字符，然后是一个单词字符（字母、数字或下划线）零个或多个空白字符，最后是一个右花括号。这部分用括号括起来，以便提取匹配到的内容。
	 * (.*?)：表示匹配任意数量的任意字符，但尽可能少地匹配。这部分用括号括起来，以便提取匹配到的内容。
	 * ================================================= 因此，整个正则表达式的意思是： 匹配一个包含在花括号中的字符串，
	 * 其中花括号中可以包含任意数量的空白字符（包括空格、制表符、换行符等）， 并且花括号中至少包含一个单词字符（字母、数字或下划线），并提取出花括号中的内容。
	 * ================================================= 最终匹配格式结构 {xx?{}} 此时可以固定
	 * 参数为2，函数名为1 完整字符串为0 字段 {{param}} 函数 {func{param}}
	 */
	private static final Pattern PATTERN = Pattern.compile("\\{\\s*(\\w*)\\s*\\{(.*?)}}");

	/**
	 * 实现BeanFactoryAware以获取容器中的 beanFactory对象, 拿到beanFactory后便可以获取容器中的bean,用于SpEl表达式的解析
	 */
	private BeanFactory beanFactory;

	private ApplicationContext applicationContext;

	private final IOperatorGetService operatorGetService;

	public LogRegxSpelParse(IOperatorGetService operatorGetService) {
		this.operatorGetService = operatorGetService;
	}

	@Override
	public boolean isRecordLog(LogSpelEvaluationContext context, String conditionSpel) {
		String isRecordLog = resolveExpression(conditionSpel, context);
		return Boolean.TRUE.toString().equals(isRecordLog);
	}

	@Override
	public LogSpelEvaluationContext buildContext(Object target, Method method, Object[] args) {
		LogSpelEvaluationContext logRecordContext = buildSpelContext(target, method, args);
		return logRecordContext;
	}

	@Override
	public LogBizInfo beforeResolve(LogSpelEvaluationContext logSpelContext, BizLog bizLog) {
		// 等待解析得模板
		List<String> waitExpressTemplate = getExpressTemplate(bizLog);

		// 函数map
		HashMap<String, String> funcMap = new HashMap<>();

		for (String template : waitExpressTemplate) {
			// 若模板不包含{
			if (!template.contains("{")) {
				continue;
			}

			Matcher matcher = PATTERN.matcher(template);
			while (matcher.find()) {
				String paramName = matcher.group(2);
				// 前置参数无结果 和错误信息
				if (paramName.contains(LogRecordConstants.POUND_KEY + LogRecordConstants.ERR_MSG)
						|| paramName.contains(LogRecordConstants.POUND_KEY + LogRecordConstants.RESULT)) {
					continue;
				}
				String funcName = matcher.group(1);

				// 如果为函数 且 是前置函数 则此处调用执行
				if (StrUtil.isNotBlank(funcName) && LogRecordFuncDiscover.isBeforeExec(funcName)) {
					Object value = LogSeplUtil.parseExpression(paramName, logSpelContext);
					String funcVal = LogRecordFuncDiscover.invokeFuncToStr(funcName, value);
					funcMap.put(getFunctionMapKey(funcName, paramName), funcVal);
				}
			}
		}
		String systemName = StrUtil.isBlank(bizLog.systemName())
				? getEnvironment().getProperty("spring.application.name") : bizLog.systemName();
		Object target = logSpelContext.getTarget();
		Method method = logSpelContext.getMethod();
		LogBizInfo logBizInfo = new LogBizInfo();
		// 记录类名 方法名
		logBizInfo.setSystemName(systemName);
		logBizInfo.setClassName(target.getClass().getName());
		logBizInfo.setMethodName(method.getName());
		logBizInfo.setFuncValMap(funcMap);
		return logBizInfo;
	}

	@Override
	public LogBizInfo afterResolve(LogBizInfo logBizOp, LogSpelEvaluationContext spelContext, BizLog bizLog) {
		//后置全局变量注册,增加中间过程产生的
		LogSeplUtil.registerGlobalParam(spelContext);
		// 等待解析得模板
		List<String> waitExpressTemplate = getExpressTemplate(bizLog);
		// 表达式map
		Map<String, String> expressionMap = logBizOp.getExpressionMap();
		Map<String, String> funcValMap = logBizOp.getFuncValMap();
		for (String template : waitExpressTemplate) {
			// 解析后得表达式值
			String expressionValue = resolveExpression(template, spelContext, funcValMap);
			expressionMap.put(template, expressionValue);
		}
		// 补充后置参数
		// operatorId 处理：优先级 注解传入 > 自定义接口实现
		String operatorId = expressionMap.getOrDefault(bizLog.operator(), operatorGetService.getOperatorId());
		logBizOp.setOperator(operatorId);
		// bizId 处理：SpEL解析 必须符合表达式
		String bizId = expressionMap.get(bizLog.bizNo());
		logBizOp.setBizNo(bizId);
		// 操作类型
		String type = expressionMap.get(bizLog.type());
		logBizOp.setType(type);
		// 判断是否需要记录结果
		if (bizLog.recordReturnValue()) {
			Object result = LogOperatorContext.peek().get(LogRecordConstants.RESULT);
			logBizOp.setResult(JSONUtil.toJsonStr(result));
		}
		Long startTime = Convert.toLong(LogOperatorContext.peek().get(LogRecordConstants.S_TIME));
		Long endTime = Convert.toLong(LogOperatorContext.peek().get(LogRecordConstants.E_TIME));
		String errorMsg = Convert.toStr(LogOperatorContext.peek().get(LogRecordConstants.ERR_MSG));
		boolean isSuccess = StrUtil.isBlank(errorMsg);
		logBizOp.setSuccess(isSuccess);
		logBizOp.setErrorMsg(errorMsg);
		logBizOp.setStartTime(startTime);
		logBizOp.setEndTime(endTime);
		logBizOp.setDetails(expressionMap.get(bizLog.detail()));
		logBizOp.setSuccessText(expressionMap.get(bizLog.success()));
		logBizOp.setFailText(expressionMap.get(bizLog.fail()));
		return logBizOp;
	}

	/**
	 * 获取前置函数映射的 key
	 * @param funcName 方法名
	 * @param param 参数
	 * @return {@link String} 返回结果
	 */
	private String getFunctionMapKey(String funcName, String param) {
		return funcName + param;
	}

	private LogSpelEvaluationContext buildSpelContext(Object target, Method method, Object[] args) {
		LogSpelEvaluationContext logRecordContext = LogSeplUtil.buildSpelContext(target, method, args);
		if (beanFactory != null) {
			// setBeanResolver 主要用于支持SpEL模板中调用指定类的方法，如：@XXService.x(#root)
			// 如果获得工厂方法自己，bean名字须要添加&前缀而不是@
			logRecordContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
		}
		return logRecordContext;
	}

	public String resolveExpression(String template, LogSpelEvaluationContext logRecordContext) {
		return resolveExpression(template, logRecordContext,
				(funcName, paramName, params) -> LogRecordFuncDiscover.invokeFuncToStr(funcName, params));
	}

	public String resolveExpression(String template, LogSpelEvaluationContext logRecordContext,
			Map<String, String> funcMap) {
		return resolveExpression(template, logRecordContext, (funcName, paramName, params) -> {
			String functionMapKey = getFunctionMapKey(funcName, paramName);
			String val = funcMap.get(functionMapKey);
			if (StrUtil.isEmpty(val)) {
				val = LogRecordFuncDiscover.invokeFuncToStr(funcName, params);
				funcMap.put(functionMapKey, val);
			}
			return val;
		});
	}

	public String resolveExpression(String template, LogSpelEvaluationContext logRecordContext, FuncEval funcEval) {
		String value;
		try {
			if (template.contains("{")) {
				// 模板搜寻匹配 若模板包含 左花括号 及支持正则匹配提取
				Matcher matcher = PATTERN.matcher(template);
				StringBuffer parsedStr = new StringBuffer();
				while (matcher.find()) {
					String paramName = matcher.group(2);
					String funcName = matcher.group(1);
					// 函数名为空 说明不是函数
					if (StrUtil.isBlank(funcName)) {
						String paramValue = LogSeplUtil.parseParamToString(paramName, logRecordContext);
						matcher.appendReplacement(parsedStr, paramValue);
					}
					else {
						// 是函数 解析参数值
						Object[] funcArgs = null;
						if (StrUtil.isNotBlank(paramName)) {
							String[] paramNameExps = paramName.split(StrUtil.COMMA);
							funcArgs = new Object[paramNameExps.length];
							for (int i = 0; i < paramNameExps.length; i++) {
								funcArgs[i] = LogSeplUtil.parseExpression(paramNameExps[i], logRecordContext);
							}
						}
						String funcVal = funcEval.evalFunc(funcName, paramName, funcArgs);
						matcher.appendReplacement(parsedStr, funcVal);
					}
				}
				matcher.appendTail(parsedStr);
				value = parsedStr.toString();
			}
			else {
				// 走默认spel表达式提取
				if (LogSeplUtil.checkParseString(template, logRecordContext)){
					value = LogSeplUtil.parseParamToString(template, logRecordContext);
				}else{
					value=template;
				}

			}
		} catch (Exception e) {
			log.error("解析表达式[{}],出现异常",template,e);
			value=template;
		}
		return value;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	/**
	 * 获取不为空的待解析模板 从这个List里面我们也可以知道，哪些参数需要符合SpEl表达式
	 * @param bizLog
	 * @return
	 */
	private List<String> getExpressTemplate(BizLog bizLog) {
		Set<String> set = new HashSet<>();
		set.addAll(Arrays.asList(bizLog.bizNo(), bizLog.detail(), bizLog.operator(), bizLog.success(), bizLog.fail(),
				bizLog.condition()));
		return set.stream().filter(s -> !ObjectUtils.isEmpty(s) && String.class.isAssignableFrom(s.getClass()))
				.collect(Collectors.toList());
	}

	private Environment getEnvironment() {
		return applicationContext.getEnvironment();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}

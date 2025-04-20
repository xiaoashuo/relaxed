package com.relaxed.common.log.biz.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import com.relaxed.common.log.biz.annotation.BizLog;
import com.relaxed.common.log.biz.constant.LogRecordConstants;
import com.relaxed.common.log.biz.context.LogRecordContext;
import com.relaxed.common.log.biz.discover.LogRecordFuncDiscover;
import com.relaxed.common.log.biz.function.FuncEval;
import com.relaxed.common.log.biz.model.AttributeModel;
import com.relaxed.common.log.biz.model.DiffMeta;
import com.relaxed.common.log.biz.model.LogBizInfo;
import com.relaxed.common.log.biz.service.IDataHandler;
import com.relaxed.common.log.biz.service.ILogBizEnhance;
import com.relaxed.common.log.biz.service.ILogParse;
import com.relaxed.common.log.biz.service.IOperatorGetService;
import com.relaxed.common.log.biz.spel.LogSpelUtil;
import com.relaxed.common.log.biz.spel.LogSpelEvaluationContext;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.slf4j.MDC;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.env.Environment;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
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
 * 日志正则表达式和 SpEL 解析器实现类 该类负责解析日志注解中的表达式，支持正则表达式和 SpEL 表达式的解析 主要功能包括： 1. 解析条件表达式判断是否记录日志 2.
 * 构建 SpEL 上下文环境 3. 解析前置和后置表达式 4. 处理函数调用和参数替换 5. 支持对象差异比较
 *
 * @author Yakir
 */
@Slf4j
public class LogRegxSpelParse implements ILogParse, BeanFactoryAware, ApplicationContextAware {

	/**
	 * 正则表达式模式，用于匹配花括号中的表达式 格式说明： - {param}：匹配简单参数 - {func{param}}：匹配函数调用 匹配规则： 1.
	 * 花括号中可以包含任意数量的空白字符 2. 花括号中至少包含一个单词字符（字母、数字或下划线） 3. 支持嵌套花括号用于函数调用
	 */
	private static final Pattern PATTERN = Pattern.compile("\\{\\s*(\\w*)\\s*\\{(.*?)}}");

	/**
	 * Spring Bean 工厂，用于获取容器中的 Bean 主要用于支持 SpEL 表达式中调用指定类的方法
	 */
	private BeanFactory beanFactory;

	/**
	 * Spring 应用上下文，用于获取环境配置等信息
	 */
	private ApplicationContext applicationContext;

	/**
	 * 操作人获取服务，用于获取当前操作人信息
	 */
	private final IOperatorGetService operatorGetService;

	/**
	 * 日志业务增强服务，用于对日志信息进行额外处理
	 */
	private final ILogBizEnhance iLogBizEnhance;

	/**
	 * 数据处理器，用于处理对象差异比较
	 */
	private final IDataHandler dataHandler;

	/**
	 * 构造函数，初始化必要的服务
	 * @param operatorGetService 操作人获取服务
	 * @param iLogBizEnhance 日志业务增强服务
	 * @param dataHandler 数据处理器
	 */
	public LogRegxSpelParse(IOperatorGetService operatorGetService, ILogBizEnhance iLogBizEnhance,
			IDataHandler dataHandler) {
		this.operatorGetService = operatorGetService;
		this.iLogBizEnhance = iLogBizEnhance;
		this.dataHandler = dataHandler;
	}

	/**
	 * 判断是否记录日志
	 * @param context SpEL 上下文
	 * @param conditionSpel 条件表达式
	 * @return 是否记录日志
	 */
	@Override
	public boolean isRecordLog(LogSpelEvaluationContext context, String conditionSpel) {
		String isRecordLog = resolveExpression(conditionSpel, context);
		return Boolean.TRUE.toString().equals(isRecordLog);
	}

	/**
	 * 构建 SpEL 上下文
	 * @param target 目标对象
	 * @param method 方法
	 * @param args 参数
	 * @return SpEL 上下文
	 */
	@Override
	public LogSpelEvaluationContext buildContext(Object target, Method method, Object[] args) {
		LogSpelEvaluationContext logRecordContext = LogSpelUtil.buildSpelContext(target, method, args);
		if (beanFactory != null) {
			logRecordContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
		}
		return logRecordContext;
	}

	/**
	 * 前置处理，解析前置表达式并构建日志信息
	 * @param logSpelContext SpEL 上下文
	 * @param bizLog 业务日志注解
	 * @return 日志业务信息
	 */
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
					Object[] funcArgs = LogSpelUtil.parseParamStrToValArr(logSpelContext, paramName);
					String funcVal = LogRecordFuncDiscover.invokeFuncToStr(funcName, funcArgs);
					funcMap.put(getFunctionMapKey(funcName, paramName), funcVal);
				}
			}
		}
		String systemName = StrUtil.isBlank(bizLog.systemName())
				? getEnvironment().getProperty("spring.application.name") : bizLog.systemName();
		String moduleName = bizLog.moduleName();
		Object target = logSpelContext.getTarget();
		Method method = logSpelContext.getMethod();
		LogBizInfo logBizInfo = new LogBizInfo();
		// 记录类名 方法名
		logBizInfo.setSystemName(systemName);
		logBizInfo.setModuleName(moduleName);
		logBizInfo.setClassName(target.getClass().getName());
		logBizInfo.setMethodName(method.getName());
		logBizInfo.setFuncValMap(funcMap);
		return logBizInfo;
	}

	/**
	 * 后置处理，解析后置表达式并完善日志信息
	 * @param logBizOp 日志业务信息
	 * @param spelContext SpEL 上下文
	 * @param bizLog 业务日志注解
	 * @return 完善后的日志业务信息
	 */
	@Override
	public LogBizInfo afterResolve(LogBizInfo logBizOp, LogSpelEvaluationContext spelContext, BizLog bizLog) {
		// 后置全局变量注册,增加中间过程产生的
		LogSpelUtil.registerGlobalParam(spelContext);
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
		String traceId = MDC.get(LogRecordConstants.TRACE_ID);
		logBizOp.setTraceId(traceId);
		// operatorId 处理：优先级 注解传入 > 自定义接口实现
		String operatorId = expressionMap.getOrDefault(bizLog.operator(), operatorGetService.getOperator());
		logBizOp.setOperator(operatorId);
		// bizId 处理：SpEL解析 必须符合表达式
		String bizId = expressionMap.get(bizLog.bizNo());
		logBizOp.setBizNo(bizId);
		// 操作类型
		String type = expressionMap.get(bizLog.type());
		logBizOp.setType(type);
		// 判断是否需要记录结果
		if (bizLog.recordReturnValue()) {
			Object result = LogRecordContext.peek().get(LogRecordConstants.RESULT);
			if (ObjectUtil.isNotEmpty(result)) {
				logBizOp.setResult(ClassUtil.isBasicType(result.getClass())
						? StrUtil.str(result, StandardCharsets.UTF_8) : JSONUtil.toJsonStr(result));
			}
		}
		Long startTime = Convert.toLong(LogRecordContext.peek().get(LogRecordConstants.S_TIME));
		Long endTime = Convert.toLong(LogRecordContext.peek().get(LogRecordConstants.E_TIME));
		String errorMsg = Convert.toStr(LogRecordContext.peek().get(LogRecordConstants.ERR_MSG));
		boolean isSuccess = StrUtil.isBlank(errorMsg);
		logBizOp.setSuccess(isSuccess);
		logBizOp.setErrorMsg(errorMsg);
		logBizOp.setStartTime(startTime);
		logBizOp.setEndTime(endTime);
		logBizOp.setDetails(expressionMap.get(bizLog.detail()));
		logBizOp.setSuccessText(expressionMap.get(bizLog.success()));
		logBizOp.setFailText(expressionMap.get(bizLog.fail()));

		// 差异对象提取
		List<DiffMeta> diffMetaList = (List<DiffMeta>) LogRecordContext.peek().get(LogRecordConstants.DIFF_KEY);
		Map<String, List<AttributeModel>> diffResult = logBizOp.getDiffResult();
		if (CollectionUtil.isNotEmpty(diffMetaList)) {
			for (DiffMeta diffMeta : diffMetaList) {
				String diffKey = diffMeta.getDiffKey();
				// 若包含diff key 则直接跳过
				if (diffResult.containsKey(diffKey)) {
					continue;
				}
				List<AttributeModel> attributeModels = dataHandler.diffObject(diffMeta);
				diffResult.put(diffKey, attributeModels);
			}
		}

		// 日志信息 增强
		iLogBizEnhance.enhance(logBizOp, spelContext);

		return logBizOp;
	}

	/**
	 * 获取前置函数映射的 key
	 * @param funcName 函数名
	 * @param param 参数
	 * @return 函数映射 key
	 */
	private String getFunctionMapKey(String funcName, String param) {
		return funcName + param;
	}

	/**
	 * 解析表达式
	 * @param template 表达式模板
	 * @param logRecordContext SpEL 上下文
	 * @return 解析结果
	 */
	public String resolveExpression(String template, LogSpelEvaluationContext logRecordContext) {
		return resolveExpression(template, logRecordContext,
				(funcName, paramName, params) -> LogRecordFuncDiscover.invokeFuncToStr(funcName, params));
	}

	/**
	 * 解析表达式，支持函数缓存
	 * @param template 表达式模板
	 * @param logRecordContext SpEL 上下文
	 * @param funcMap 函数缓存
	 * @return 解析结果
	 */
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

	/**
	 * 解析表达式，支持自定义函数求值
	 * @param template 表达式模板
	 * @param logRecordContext SpEL 上下文
	 * @param funcEval 函数求值器
	 * @return 解析结果
	 */
	public String resolveExpression(String template, LogSpelEvaluationContext logRecordContext, FuncEval funcEval) {
		String value;
		try {
			if (template.contains("{")) {
				// 模板搜寻匹配 若模板包含 左花括号 及支持正则匹配提取
				value = innerResolveExpress(template, logRecordContext, funcEval);
			}
			else {
				// 走默认spel表达式提取
				value = LogSpelUtil.parseParamToString(template, logRecordContext);
			}
		}
		catch (Exception e) {
			log.error("解析表达式[{}],出现异常", template, e);
			value = template;
		}
		return value;
	}

	/**
	 * 内部解析表达式实现
	 * @param template 表达式模板
	 * @param logRecordContext SpEL 上下文
	 * @param funcEval 函数求值器
	 * @return 解析结果
	 */
	private static String innerResolveExpress(String template, LogSpelEvaluationContext logRecordContext,
			FuncEval funcEval) {
		Matcher matcher = PATTERN.matcher(template);
		StringBuffer parsedStr = new StringBuffer();
		while (matcher.find()) {
			String paramName = matcher.group(2);
			String funcName = matcher.group(1);
			// 函数名为空 说明不是函数
			if (StrUtil.isBlank(funcName)) {
				String paramValue = LogSpelUtil.parseParamToString(paramName, logRecordContext);
				matcher.appendReplacement(parsedStr, quoteJson(paramValue));
			}
			else {
				// 是函数 解析参数值
				Object[] funcArgs = LogSpelUtil.parseParamStrToValArr(logRecordContext, paramName);
				String funcVal = funcEval.evalFunc(funcName, paramName, funcArgs);
				funcVal = funcVal == null ? "" : funcVal;
				matcher.appendReplacement(parsedStr, quoteJson(funcVal));
			}
		}
		matcher.appendTail(parsedStr);
		return parsedStr.toString();
	}

	/**
	 * 对 JSON 字符串进行引号转义
	 * @param jsonStr JSON 字符串
	 * @return 转义后的字符串
	 */
	public static String quoteJson(String jsonStr) {
		if (StrUtil.isBlank(jsonStr)) {
			return jsonStr;
		}
		// 非json类型 保持原值
		if (!JSONUtil.isTypeJSON(jsonStr)) {
			return jsonStr;
		}
		String escapedParamValue = JSONUtil.quote(jsonStr);
		// 移除最外层的引号（因为quote方法会给字符串添加引号）
		escapedParamValue = escapedParamValue.substring(1, escapedParamValue.length() - 1);
		return escapedParamValue;
	}

	/**
	 * 设置 Bean 工厂
	 * @param beanFactory Bean 工厂
	 */
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	/**
	 * 获取表达式模板列表
	 * @param bizLog 业务日志注解
	 * @return 表达式模板列表
	 */
	protected List<String> getExpressTemplate(BizLog bizLog) {
		Set<String> set = new HashSet<>();
		set.addAll(Arrays.asList(bizLog.bizNo(), bizLog.detail(), bizLog.operator(), bizLog.success(), bizLog.fail(),
				bizLog.type()));
		return set.stream().filter(s -> !ObjectUtils.isEmpty(s) && String.class.isAssignableFrom(s.getClass()))
				.collect(Collectors.toList());
	}

	/**
	 * 获取环境配置
	 * @return 环境配置
	 */
	private Environment getEnvironment() {
		return applicationContext.getEnvironment();
	}

	/**
	 * 设置应用上下文
	 * @param applicationContext 应用上下文
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}

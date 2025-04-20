package com.relaxed.common.log.operation.service;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 操作日志处理器抽象类 提供了操作日志处理的基础实现,包括: 1. 忽略特定类型的参数记录 2. 获取方法参数的JSON字符串表示 子类可以继承此类并实现具体的日志处理逻辑
 *
 * @author hccake
 * @since 1.0.0
 */
@Slf4j
public abstract class AbstractOperationLogHandler<T> implements OperationLogHandler<T> {

	/**
	 * 需要忽略的参数类型列表 默认包含: - ServletRequest: HTTP请求对象 - ServletResponse: HTTP响应对象 -
	 * MultipartFile: 文件上传对象
	 */
	private final List<Class<?>> ignoredParamClasses = ListUtil.toList(ServletRequest.class, ServletResponse.class,
			MultipartFile.class);

	/**
	 * 添加需要忽略的参数类型 当方法参数类型在忽略列表中时,将不会记录该参数的具体内容
	 * @param clazz 需要忽略的参数类型
	 */
	public void addIgnoredParamClass(Class<?> clazz) {
		ignoredParamClasses.add(clazz);
	}

	/**
	 * 获取方法参数的JSON字符串表示 将方法的参数名和参数值转换为JSON格式的字符串 对于忽略类型的参数,将只记录其类型信息
	 * @param joinPoint 切点对象,包含方法调用的上下文信息
	 * @return 方法参数的JSON字符串,如果参数为空则返回null
	 */
	public String getParams(ProceedingJoinPoint joinPoint) {
		// 获取方法签名
		Signature signature = joinPoint.getSignature();
		String strClassName = joinPoint.getTarget().getClass().getName();
		String strMethodName = signature.getName();
		MethodSignature methodSignature = (MethodSignature) signature;
		log.debug("[getParams]，获取方法参数[类名]:{},[方法]:{}", strClassName, strMethodName);

		String[] parameterNames = methodSignature.getParameterNames();
		Object[] args = joinPoint.getArgs();
		if (ArrayUtil.isEmpty(parameterNames)) {
			return null;
		}
		Map<String, Object> paramsMap = new HashMap<>();
		for (int i = 0; i < parameterNames.length; i++) {
			Object arg = args[i];
			if (arg == null) {
				paramsMap.put(parameterNames[i], null);
				continue;
			}
			Class<?> argClass = arg.getClass();
			// 忽略部分类型的参数记录
			for (Class<?> ignoredParamClass : ignoredParamClasses) {
				if (ignoredParamClass.isAssignableFrom(argClass)) {
					arg = "ignored param type: " + argClass;
					break;
				}
			}
			paramsMap.put(parameterNames[i], arg);
		}

		String params = "";
		try {
			// 入参类中的属性可以通过注解进行数据落库脱敏以及忽略等操作
			params = JSONUtil.toJsonStr(paramsMap);
		}
		catch (Exception e) {
			log.error("[getParams]，获取方法参数异常，[类名]:{},[方法]:{}", strClassName, strMethodName, e);
		}

		return params;
	}

}

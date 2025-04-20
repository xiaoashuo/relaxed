package com.relaxed.common.log.biz.discover;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.relaxed.common.log.biz.annotation.LogFunc;
import com.relaxed.common.log.biz.constant.LogRecordConstants;
import com.relaxed.common.log.biz.function.IParseFunc;
import com.relaxed.common.log.biz.model.FuncMeta;

import com.relaxed.common.log.biz.util.LogClassUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 日志函数发现器，负责注册和管理日志模板中可用的函数。 该组件实现了 ApplicationContextAware 接口，在 Spring 容器初始化时自动注册函数。
 * 支持两种函数注册方式： 1. 通过 {@link LogFunc} 注解标记的函数 2. 实现 {@link IParseFunc} 接口的函数
 * 注册的函数可以在日志模板中通过 SpEL 表达式调用。
 *
 * @author Yakir
 * @since 1.0.0
 */
@Component
@Slf4j
public class LogRecordFuncDiscover implements ApplicationContextAware {

	/**
	 * 函数映射表，存储所有注册的函数元数据 key: 函数名称（可能包含命名空间） value: 函数元数据，包含目标对象、方法、执行时机等信息
	 */
	private static Map<String, FuncMeta> functionMap = new HashMap<>();

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// 注册接口函数
		registerInterfaceFuncs(applicationContext);
		// 注册注解函数
		registerAnnotationFuncs(applicationContext);
	}

	/**
	 * 注册通过 {@link LogFunc} 注解标记的函数 支持类级别和方法级别的注解，可以指定命名空间和函数名称
	 * @param applicationContext Spring 应用上下文
	 */
	private static void registerAnnotationFuncs(ApplicationContext applicationContext) {
		Map<String, Object> beans = applicationContext.getBeansWithAnnotation(LogFunc.class);
		for (Object value : beans.values()) {
			Method[] methods = ClassUtil.getDeclaredMethods(value.getClass());
			Map<Method, LogFunc> annotationMap = new HashMap<>();
			for (Method method : methods) {
				LogFunc annotation = AnnotatedElementUtils.findMergedAnnotation(method, LogFunc.class);
				if (annotation != null) {
					annotationMap.put(method, annotation);
				}
			}
			annotationMap.forEach((method, annotation) -> {
				boolean isStaticMethod = ClassUtil.isStatic(method);
				String namespace = annotation.namespace();
				String funcName = annotation.funcName();
				String around = annotation.around();
				// 真实函数名称
				String regFuncName;
				if (StrUtil.isBlank(namespace) && StrUtil.isBlank(funcName)) {
					regFuncName = method.getName();
				}
				else {
					regFuncName = StrUtil.join(StrPool.UNDERLINE, namespace, funcName);
				}

				if (isStaticMethod) {
					// 静态方法直接注册
					FuncMeta funcMeta = new FuncMeta(regFuncName, true, value, method, around);
					functionMap.put(regFuncName, funcMeta);
					log.info("LogRecord register function type[annotation] alias[{}] func_address[{}] static method",
							regFuncName, method);
				}
				else {
					// 非静态方法包装成静态方法 推荐使用 Javassist 此处换种方式 保留原始对象与方法的引用
					FuncMeta funcMeta = new FuncMeta(regFuncName, false, value, method, around);
					functionMap.put(regFuncName, funcMeta);
					log.info(
							"LogRecord register function type[annotation] alias[{}] func_address[{}] non static method",
							regFuncName, method);
				}
			});
		}
	}

	/**
	 * 注册实现 {@link IParseFunc} 接口的函数 这些函数通过接口定义的方式提供，支持命名空间和函数名称的配置
	 * @param applicationContext Spring 应用上下文
	 */
	private static void registerInterfaceFuncs(ApplicationContext applicationContext) {
		Map<String, IParseFunc> iFuncs = applicationContext.getBeansOfType(IParseFunc.class);
		for (IParseFunc func : iFuncs.values()) {
			String fullFuncName = func.namespace() + StrPool.UNDERLINE + func.name();
			Method method = ClassUtil.getDeclaredMethod(func.getClass(), "apply", Object[].class);
			FuncMeta funcMeta = new FuncMeta(fullFuncName, false, func, method, func.around());
			functionMap.put(fullFuncName, funcMeta);
			log.info("LogRecord register function type[interface] alias[{}] func_address[{}]", fullFuncName, func);
		}
	}

	/**
	 * 获取所有注册的函数映射表
	 * @return 函数名称到函数元数据的映射
	 */
	public static Map<String, FuncMeta> getFunctionMap() {
		return functionMap;
	}

	/**
	 * 根据函数名称获取函数元数据
	 * @param funcname 函数名称
	 * @return 函数元数据，如果未找到则返回 null
	 */
	public static FuncMeta getFunctionMeta(String funcname) {
		return functionMap.get(funcname);
	}

	/**
	 * 手动注册一个函数
	 * @param funcName 函数名称
	 * @param funcMeta 函数元数据
	 */
	public static void regFunc(String funcName, FuncMeta funcMeta) {
		functionMap.put(funcName, funcMeta);
	}

	/**
	 * 判断函数是否为前置函数（在目标方法执行前调用）
	 * @param funcName 函数名称
	 * @return 如果是前置函数返回 true，否则返回 false
	 */
	public static boolean isBeforeExec(String funcName) {
		FuncMeta funcMeta = functionMap.get(funcName);
		return funcMeta != null && LogRecordConstants.BEFORE_FUNC.equals(funcMeta.getAround());
	}

	/**
	 * 调用指定的函数
	 * @param funname 函数名称
	 * @param arguments 函数参数
	 * @return 函数执行结果
	 * @throws RuntimeException 如果函数执行过程中发生异常
	 */
	public static Object invokeFunc(String funname, Object... arguments) {
		FuncMeta funcMeta = getFunctionMeta(funname);
		Object targetFuncObj = funcMeta.getTarget();
		String orgMethodName = funcMeta.getMethod().getName();
		Class<?>[] parameterTypes = funcMeta.getParameterTypes();
		try {
			if (funcMeta.isStatic()) {
				// 静态方法执行
				Method method = targetFuncObj.getClass().getMethod(orgMethodName, parameterTypes);
				Object result = LogClassUtil.invokeRaw(targetFuncObj, method, arguments);
				return result;
			}
			else {
				Method method = targetFuncObj.getClass().getMethod(orgMethodName, parameterTypes);
				Object result = LogClassUtil.invokeRaw(targetFuncObj, method, arguments);
				return result;
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 调用指定的函数并将结果转换为字符串
	 * @param funname 函数名称
	 * @param arguments 函数参数
	 * @return 函数执行结果的字符串表示，如果结果为 null 则返回 null
	 */
	public static String invokeFuncToStr(String funname, Object... arguments) {
		Object result = invokeFunc(funname, arguments);
		if (result != null) {
			return result instanceof String ? (String) result : JSONUtil.toJsonStr(result);
		}
		return null;
	}

}

package com.relaxed.common.log.biz.spel;

import com.relaxed.common.log.biz.discover.LogRecordFuncDiscover;
import com.relaxed.common.log.biz.model.FuncMeta;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.MethodExecutor;
import org.springframework.expression.MethodResolver;

import java.util.List;

/**
 * 日志方法解析器 该解析器用于在 SpEL 表达式中解析和调用日志相关的方法 主要功能包括： 1. 根据方法名查找对应的函数元数据 2. 返回方法执行器用于执行方法调用 3.
 * 支持自定义函数的动态解析和执行
 *
 * 参考 {@link org.springframework.expression.spel.support.ReflectiveMethodResolver}
 *
 * @author Yakir
 */
public class LogMethodResolver implements MethodResolver {

	/**
	 * 解析方法 根据方法名查找对应的函数元数据，并返回方法执行器
	 * @param context 求值上下文
	 * @param targetObject 目标对象
	 * @param methodName 方法名
	 * @param argumentTypes 参数类型列表
	 * @return 方法执行器，如果未找到对应方法则返回 null
	 * @throws AccessException 访问异常
	 */
	@Override
	public MethodExecutor resolve(EvaluationContext context, Object targetObject, String methodName,
			List<TypeDescriptor> argumentTypes) throws AccessException {
		FuncMeta functionMeta = LogRecordFuncDiscover.getFunctionMeta(methodName);
		return functionMeta == null ? null : functionMeta.getMethodExecutor();
	}

}

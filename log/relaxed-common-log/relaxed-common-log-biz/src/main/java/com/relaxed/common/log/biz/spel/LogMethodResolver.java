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
 * @author Yakir
 * @Topic LogMethodResolver
 * @Description 参考
 * {@link org.springframework.expression.spel.support.ReflectiveMethodResolver}
 * @date 2023/12/19 9:29
 * @Version 1.0
 */
public class LogMethodResolver implements MethodResolver {

	@Override
	public MethodExecutor resolve(EvaluationContext context, Object targetObject, String methodName,
			List<TypeDescriptor> argumentTypes) throws AccessException {

		FuncMeta functionMeta = LogRecordFuncDiscover.getFunctionMeta(methodName);
		return functionMeta == null ? null : functionMeta.getMethodExecutor();

	}

}

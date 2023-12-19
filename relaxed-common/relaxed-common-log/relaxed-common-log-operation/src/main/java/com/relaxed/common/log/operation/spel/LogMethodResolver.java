package com.relaxed.common.log.operation.spel;

import com.relaxed.common.log.operation.discover.FuncMeta;
import com.relaxed.common.log.operation.discover.LogRecordFuncDiscover;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.MethodExecutor;
import org.springframework.expression.MethodResolver;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.support.ReflectiveMethodResolver;

import java.util.List;

/**
 * @author Yakir
 * @Topic LogMethodResolver
 * @Description  参考 {@link org.springframework.expression.spel.support.ReflectiveMethodResolver}
 * @date 2023/12/19 9:29
 * @Version 1.0
 */
public class LogMethodResolver implements MethodResolver {
    @Override
    public MethodExecutor resolve(EvaluationContext context, Object targetObject, String methodName, List<TypeDescriptor> argumentTypes) throws AccessException {

        FuncMeta functionMeta = LogRecordFuncDiscover.getFunctionMeta(methodName);
        return functionMeta==null?null:functionMeta.getMethodExecutor();


    }



}

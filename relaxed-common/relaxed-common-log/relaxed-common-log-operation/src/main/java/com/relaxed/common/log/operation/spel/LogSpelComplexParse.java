package com.relaxed.common.log.operation.spel;

import com.relaxed.common.core.util.SpELUtil;
import com.relaxed.common.log.operation.aspect.LogOperatorContext;
import com.relaxed.common.log.operation.discover.FuncMeta;
import com.relaxed.common.log.operation.discover.LogRecordFuncDiscover;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

/**
 * @author Yakir
 * @Topic LogSpelComplexParse
 * @Description
 * @date 2023/12/18 14:44
 * @Version 1.0
 */
public class LogSpelComplexParse {
    /**
     * 方法参数获取
     */
    private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();



    public void test(){

        String spelExpression="";
//        LogSpelEvaluationContext spElContext = new LogSpelEvaluationContext(target, method, args,
//                getParameterNameDiscoverer());
        LogSpelEvaluationContext spElContext = null;

        //注册全局函数
        registerGlobalFuncs(spElContext);
        String detailValue = SpELUtil.parseValueToString(spElContext, spelExpression);
    }


    /**
     * 注册全局的spel方法
     * @param spElContext
     */
    private void registerGlobalFuncs(StandardEvaluationContext spElContext) {
        Map<String, FuncMeta> functionMap = LogRecordFuncDiscover.getFunctionMap();

        functionMap.forEach((methodName,funcMeta)-> spElContext.registerFunction(methodName,funcMeta.getMethod()));
    }


}


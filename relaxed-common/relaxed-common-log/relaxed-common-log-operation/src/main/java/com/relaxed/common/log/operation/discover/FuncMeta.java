package com.relaxed.common.log.operation.discover;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.MethodExecutor;
import org.springframework.expression.TypedValue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Yakir
 * @Topic FuncMeta
 * @Description
 * @date 2023/12/15 10:01
 * @Version 1.0
 */
@Data
public class FuncMeta {
    /**
     * 注册方法名
     */
    private String regFuncName;




    /**
     * 是否静态方法
     */
    private boolean isStatic;
    /**
     * 目标对象
     */
    private Object target;


    /**
     * 目标方法
     */
    private Method method;

    /**
     * 执行时机
     */
    private String around;

    private MethodExecutor methodExecutor;

    private Class<?>[] parameterTypes;

    public FuncMeta() {

    }

    public FuncMeta(String regFuncName, boolean isStatic, Object target, Method method) {
        this.regFuncName = regFuncName;
        this.isStatic = isStatic;
        this.target = target;
        this.method = method;
        this.methodExecutor=initMethodExecutor(this);
        this.parameterTypes= method.getParameterTypes();
    }

    public MethodExecutor getMethodExecutor() {
        return methodExecutor;
    }

    private MethodExecutor initMethodExecutor(FuncMeta funcMeta) {
        // 自定义 MethodExecutor
//        return new MethodExecutor() {
//            @Override
//            public TypedValue execute(EvaluationContext context, Object target, Object... arguments) throws AccessException {
//                // 在这里编写方法的实际逻辑
//                if (arguments != null && arguments.length > 0) {
//                    String argument = (String) arguments[0];
//
//                    return new TypedValue("Modified: " + argument);
//                }
//                throw new AccessException("Invalid arguments for myMethod");
//            }
//        };
        return new MyMethodExecutor(funcMeta);
    }

    // 自定义 MethodExecutor
    @RequiredArgsConstructor
     class MyMethodExecutor implements MethodExecutor {
        public final FuncMeta funcMeta;
        @Override
        public TypedValue execute(EvaluationContext context, Object rootObj, Object... arguments) throws AccessException {
            // 在这里编写方法的实际逻辑
            if (arguments != null && arguments.length > 0) {
                Method orgMethod = funcMeta.getMethod();
                String orgMethodName = orgMethod.getName();
//                Object targetFuncObj = funcMeta.getTarget();
//                Class<?>[] parameterTypes = funcMeta.getParameterTypes();
                try {
                    Object result = LogRecordFuncDiscover.invokeFunc(orgMethodName, arguments);
                    return new TypedValue(result);
//                    if (funcMeta.isStatic()){
//                        //静态方法执行
//                        Class target = (Class) targetFuncObj;
//                        Method method = target.getMethod(orgMethodName, parameterTypes);
//                        Object result = method.invoke(target, arguments);
//                        return new TypedValue(result);
//
//                }else{
//                    Method method = targetFuncObj.getClass().getMethod(orgMethodName, parameterTypes);
//
//                    Object result = ReflectUtil.invokeRaw(targetFuncObj,method,arguments);
//                    return new TypedValue(result);
//                }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            throw new AccessException("Invalid arguments for myMethod");
        }
    }



}

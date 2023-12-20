package com.relaxed.common.log.operation.discover.func;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.relaxed.common.log.operation.annotation.LogFunc;
import com.relaxed.common.log.operation.constants.LogRecordConstants;
import com.relaxed.common.log.operation.util.LogClassUtil;
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
 * @author Yakir
 * @Topic LogFuncDiscover
 * @Description
 * @date 2023/12/14 18:08
 * @Version 1.0
 */
@Component
@Slf4j
public class LogRecordFuncDiscover implements ApplicationContextAware {

    /**
     * 注解函数 非静态类
     */
    private static Map<String, FuncMeta> functionMap = new HashMap<>();



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        //注册接口函数
        registerInterfaceFuncs(applicationContext);
        //注册注解函数
        registerAnnotationFuncs(applicationContext);

    }

    private static void registerAnnotationFuncs(ApplicationContext applicationContext) {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(LogFunc.class);
        for (Object value : beans.values()) {
            Method[] methods = ClassUtil.getDeclaredMethods(value.getClass());
            Map<Method, LogFunc> annotationMap = new HashMap<>();
            for (Method method : methods) {
                LogFunc annotation = AnnotatedElementUtils.findMergedAnnotation(method, LogFunc.class);
                if (annotation!=null){
                    annotationMap.put(method,annotation);
                }
            }
            annotationMap.forEach((method,annotation)->{
                boolean isStaticMethod = ClassUtil.isStatic(method);
                String namespace = annotation.namespace();
                String funcName = annotation.funcName();
                //真实函数名称
                String regFuncName;
                if (StrUtil.isBlank(namespace)&&StrUtil.isBlank(funcName)){
                    regFuncName=method.getName();
                }else{
                    regFuncName = StrUtil.join(StrPool.UNDERLINE, namespace, funcName);
                }

                if (isStaticMethod){
                    //静态方法直接注册
                    FuncMeta funcMeta = new FuncMeta(regFuncName, true, value, method);
                    functionMap.put(regFuncName,funcMeta);
                    log.info("LogRecord register static custom function [{}] as name [{}]", method, regFuncName);
                }else{
                    //非静态方法包装成静态方法 推荐使用 Javassist  此处换种方式 保留原始对象与方法的引用
                    FuncMeta funcMeta = new FuncMeta(regFuncName, false, value, method);
                    functionMap.put(regFuncName,funcMeta);
                    log.info("LogRecord register non static custom function [{}] as name [{}]", method, regFuncName);
                }
            });


        }
    }




    private static void registerInterfaceFuncs(ApplicationContext applicationContext) {
        Map<String, IParseFunc> iFuncs = applicationContext.getBeansOfType(IParseFunc.class);
        for (IParseFunc func : iFuncs.values()) {

            String fullFuncName = func.namespace() + StrPool.UNDERLINE + func.name();
            Method method = ClassUtil.getDeclaredMethod(func.getClass(), "apply", Object[].class);
            FuncMeta funcMeta = new FuncMeta(fullFuncName, false, func, method);
            functionMap.put(fullFuncName,funcMeta);
            ///iFuncServiceMap.put(fullFuncName,func);
            log.info("LogRecord register interface impl custom function [{}] as name [{}]", func, fullFuncName);

        }
    }


    public static Map<String, FuncMeta> getFunctionMap() {
        return functionMap;
    }


    public static FuncMeta getFunctionMeta(String funcname){
        return functionMap.get(funcname);
    }

    public static void regFunc(String funcName,FuncMeta funcMeta){
        functionMap.put(funcName,funcMeta);
    }
    public static boolean isBeforeExec(String funcName){
        FuncMeta funcMeta = functionMap.get(funcName);
        return funcMeta != null && LogRecordConstants.BEFORE_FUNC.equals(funcMeta.getAround());
    }


    public static Object invokeFunc(String funname,Object... arguments){
        FuncMeta funcMeta = getFunctionMeta(funname);
        Object targetFuncObj = funcMeta.getTarget();
        String orgMethodName=funcMeta.getMethod().getName();
        Class<?>[] parameterTypes = funcMeta.getParameterTypes();
        try {
            if (funcMeta.isStatic()){
                //静态方法执行
                Class target = (Class) targetFuncObj;
                Method method = target.getMethod(orgMethodName, parameterTypes);
                Object result = method.invoke(target, arguments);
                return result;

            }else{
                Method method = targetFuncObj.getClass().getMethod(orgMethodName, parameterTypes);

                Object result = LogClassUtil.invokeRaw(targetFuncObj,method,arguments);
                return result;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String invokeFuncToStr(String funname,Object... arguments){
        Object result = invokeFunc(funname, arguments);

        if (result != null) {
            return result instanceof String ? (String) result : JSONUtil.toJsonStr(result);
        }
        return null;
    }


}

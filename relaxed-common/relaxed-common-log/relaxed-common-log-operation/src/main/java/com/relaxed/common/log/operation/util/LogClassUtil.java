package com.relaxed.common.log.operation.util;

import cn.hutool.core.bean.NullWrapperBean;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.InvocationTargetRuntimeException;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.reflect.MethodHandleUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Yakir
 * @Topic LogClassUtil
 * @Description
 * @date 2023/12/20 9:59
 * @Version 1.0
 */
public class LogClassUtil {
    /**
     * 执行方法
     *
     * <p>
     * 对于用户传入参数会做必要检查，包括：
     *
     * <pre>
     *     1、忽略多余的参数
     *     2、参数不够补齐默认值
     *     3、传入参数为null，但是目标参数类型为原始类型，做转换
     * </pre>
     *
     * @param <T>    返回对象类型
     * @param obj    对象，如果执行静态方法，此值为{@code null}
     * @param method 方法（对象方法或static方法都可）
     * @param args   参数对象
     * @return 结果
     * @throws InvocationTargetRuntimeException 目标方法执行异常
     * @throws UtilException                    {@link IllegalAccessException}异常的包装
     * @since 5.8.1
     */
    @SuppressWarnings("unchecked")
    public static <T> T invokeRaw(Object obj, Method method, Object... args) throws InvocationTargetException, IllegalAccessException {
        ClassUtil.setAccessible(method);

        // 检查用户传入参数：
        // 1、忽略多余的参数
        // 2、参数不够补齐默认值
        // 3、通过NullWrapperBean传递的参数,会直接赋值null
        // 4、传入参数为null，但是目标参数类型为原始类型，做转换
        // 5、传入参数类型不对应，尝试转换类型
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final Object[] actualArgs = new Object[parameterTypes.length];
        if (null != args) {
            //判断参数类型长度是否为1 且为数组
            if (parameterTypes.length==1&&parameterTypes[0].isArray()){
                //判断参数是否为1 个 若1个直接赋值 否则 强制转换
                if (args.length==1){
                    actualArgs[0] = args[0];
                }else{
                    actualArgs[0]=ArrayUtil.addAll(args);
                }
            }else{
                for (int i = 0; i < actualArgs.length; i++) {
                    if (i >= args.length || null == args[i]) {
                        // 越界或者空值
                        actualArgs[i] = ClassUtil.getDefaultValue(parameterTypes[i]);
                    } else if (args[i] instanceof NullWrapperBean) {
                        //如果是通过NullWrapperBean传递的null参数,直接赋值null
                        actualArgs[i] = null;
                    } else if (false == parameterTypes[i].isAssignableFrom(args[i].getClass())) {
                        //对于类型不同的字段，尝试转换，转换失败则使用原对象类型
                        final Object targetValue = Convert.convert(parameterTypes[i], args[i]);
                        if (null != targetValue) {
                            actualArgs[i] = targetValue;
                        }
                    } else {
                        actualArgs[i] = args[i];
                    }
                }
            }

        }

        if (method.isDefault()) {
            // 当方法是default方法时，尤其对象是代理对象，需使用句柄方式执行
            // 代理对象情况下调用method.invoke会导致循环引用执行，最终栈溢出
            return MethodHandleUtil.invokeSpecial(obj, method, args);
        }

        return (T) method.invoke(ClassUtil.isStatic(method) ? null : obj, actualArgs);
    }
}

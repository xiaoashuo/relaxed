package com.relaxed.common.core.util.lambda;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.relaxed.common.core.util.lambda.SerializedLambda;
import lombok.Data;

import javax.management.ReflectionException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Locale;

/**
 * @author Yakir
 * @Topic LambdaUtils
 * @Description
 * @date 2022/6/14 16:28
 * @Version 1.0
 */
public class LambdaUtils {

	public LambdaUtils() {
	}

	public static <T> LambdaMeta extract(SFunction<T, ?> func) {
		if (func instanceof Proxy) {
			return new IdeaProxyLambdaMeta((Proxy) func);
		}
		else {
			try {
				Method method = func.getClass().getDeclaredMethod("writeReplace");
				return new ReflectLambdaMeta(
						(SerializedLambda) ((Method) ReflectUtil.setAccessible(method)).invoke(func));
			}
			catch (Throwable var2) {
				return new ShadowLambdaMeta(SerializedLambda.extract(func));
			}
		}
	}

}

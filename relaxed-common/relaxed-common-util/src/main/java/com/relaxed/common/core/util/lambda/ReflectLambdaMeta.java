package com.relaxed.common.core.util.lambda;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;

/**
 * @author Yakir
 * @Topic ReflectLambdaMeta
 * @Description
 * @date 2022/6/14 16:38
 * @Version 1.0
 */
@Slf4j
public class ReflectLambdaMeta implements LambdaMeta {

	private static final Field FIELD_CAPTURING_CLASS;

	static {
		Field fieldCapturingClass;
		try {
			Class<SerializedLambda> aClass = SerializedLambda.class;
			fieldCapturingClass = ReflectUtil.setAccessible(aClass.getDeclaredField("capturingClass"));
		}
		catch (Throwable e) {
			// 解决高版本 jdk 的问题 gitee: https://gitee.com/baomidou/mybatis-plus/issues/I4A7I5
			log.warn(e.getMessage());
			fieldCapturingClass = null;
		}
		FIELD_CAPTURING_CLASS = fieldCapturingClass;
	}

	private final SerializedLambda lambda;

	public ReflectLambdaMeta(SerializedLambda lambda) {
		this.lambda = lambda;
	}

	@Override
	public String getImplMethodName() {
		return lambda.getImplMethodName();
	}

	@SneakyThrows
	@Override
	public Class<?> getInstantiatedClass() {
		String instantiatedMethodType = lambda.getInstantiatedMethodType();
		String instantiatedType = instantiatedMethodType.substring(2, instantiatedMethodType.indexOf(";"))
				.replace(StrPool.SLASH, StrPool.DOT);
		return ClassUtil.loadClass(instantiatedType);
	}

}

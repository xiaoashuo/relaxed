package com.relaxed.common.core.util.lambda;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ClassUtil;
import lombok.SneakyThrows;
import org.springframework.util.ClassUtils;

/**
 * @author Yakir
 * @Topic ShadowLambdaMeta
 * @Description
 * @date 2022/6/14 16:39
 * @Version 1.0
 */
public class ShadowLambdaMeta implements LambdaMeta {

	private final SerializedLambda lambda;

	public ShadowLambdaMeta(SerializedLambda lambda) {
		this.lambda = lambda;
	}

	@Override
	public String getImplMethodName() {
		return lambda.getImplMethodName();
	}

	@Override
	public Class<?> getInstantiatedClass() {

		String instantiatedMethodType = lambda.getInstantiatedMethodType();
		String instantiatedType = instantiatedMethodType.substring(2, instantiatedMethodType.indexOf(";"))
				.replace(StrPool.SLASH, StrPool.DOT);

		return ClassUtil.loadClass(instantiatedType);

	}

}

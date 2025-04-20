package com.relaxed.common.core.util.lambda;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ClassUtil;
import lombok.SneakyThrows;
import org.springframework.util.ClassUtils;

/**
 * Lambda 表达式元数据的影子实现类。
 *
 * 该实现通过包装 {@link SerializedLambda} 对象来提供 Lambda 表达式的元数据信息。 主要用于处理标准 JDK Lambda
 * 表达式的元数据提取，不涉及特殊的代理或反射机制。
 *
 * @author Yakir
 * @since 1.0
 * @see LambdaMeta
 * @see SerializedLambda
 */
public class ShadowLambdaMeta implements LambdaMeta {

	/**
	 * 被包装的序列化 Lambda 对象
	 */
	private final SerializedLambda lambda;

	/**
	 * 构造函数
	 * @param lambda 序列化的 Lambda 对象
	 */
	public ShadowLambdaMeta(SerializedLambda lambda) {
		this.lambda = lambda;
	}

	/**
	 * 获取 Lambda 表达式实现方法的名称
	 * @return Lambda 表达式对应的方法名
	 */
	@Override
	public String getImplMethodName() {
		return lambda.getImplMethodName();
	}

	/**
	 * 获取 Lambda 表达式实例化的类
	 * @return Lambda 表达式中方法所属的类
	 */
	@Override
	public Class<?> getInstantiatedClass() {
		String instantiatedMethodType = lambda.getInstantiatedMethodType();
		String instantiatedType = instantiatedMethodType.substring(2, instantiatedMethodType.indexOf(";"))
				.replace(StrPool.SLASH, StrPool.DOT);

		return ClassUtil.loadClass(instantiatedType);
	}

}

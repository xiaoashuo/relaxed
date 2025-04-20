package com.relaxed.common.core.util.lambda;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;

/**
 * 基于反射机制的 Lambda 表达式元数据实现类。
 *
 * 该实现通过反射机制获取和处理 Lambda 表达式的元数据信息，主要用于： - 处理标准 JDK Lambda 表达式 - 支持高版本 JDK 的兼容性 -
 * 提供更灵活的元数据访问方式
 *
 * 注意：该实现包含对 JDK 内部类的反射访问，在使用时需要注意 JDK 版本的兼容性。
 *
 * @author Yakir
 * @since 1.0
 * @see LambdaMeta
 * @see SerializedLambda
 */
@Slf4j
public class ReflectLambdaMeta implements LambdaMeta {

	/**
	 * 捕获类的反射字段 用于访问 SerializedLambda 中的 capturingClass 字段
	 */
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

	/**
	 * 被包装的序列化 Lambda 对象
	 */
	private final SerializedLambda lambda;

	/**
	 * 构造函数
	 * @param lambda 序列化的 Lambda 对象
	 */
	public ReflectLambdaMeta(SerializedLambda lambda) {
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
	 * 获取 Lambda 表达式实例化的类 通过反射机制解析方法类型字符串，获取实际的类对象
	 * @return Lambda 表达式中方法所属的类
	 */
	@SneakyThrows
	@Override
	public Class<?> getInstantiatedClass() {
		String instantiatedMethodType = lambda.getInstantiatedMethodType();
		String instantiatedType = instantiatedMethodType.substring(2, instantiatedMethodType.indexOf(";"))
				.replace(StrPool.SLASH, StrPool.DOT);
		return ClassUtil.loadClass(instantiatedType);
	}

}

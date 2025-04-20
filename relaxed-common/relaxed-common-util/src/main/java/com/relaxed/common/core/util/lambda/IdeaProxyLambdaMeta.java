package com.relaxed.common.core.util.lambda;

import cn.hutool.core.util.ReflectUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * IDEA 调试环境下的 Lambda 表达式元数据实现类。
 *
 * 该实现专门用于处理在 IDEA 的 Evaluate 表达式或调试环境中执行的 Lambda 表达式。 由于 IDEA 调试环境中的 Lambda
 * 表达式会被包装为特殊的代理对象，需要特殊处理才能获取其元数据。
 *
 * 主要特点： - 支持 IDEA 调试环境中的 Lambda 表达式解析 - 通过反射访问 JDK 内部类获取元数据 - 处理 DirectMethodHandle 和
 * MemberName 相关的特殊逻辑
 *
 * 注意：该实现依赖于 JDK 内部类的结构，可能随 JDK 版本变化而需要调整。
 *
 * @author Yakir
 * @since 1.0
 * @see LambdaMeta
 */
public class IdeaProxyLambdaMeta implements LambdaMeta {

	/**
	 * DirectMethodHandle 中的 member 字段
	 */
	private static final Field FIELD_MEMBER_NAME;

	/**
	 * MemberName 中的 clazz 字段
	 */
	private static final Field FIELD_MEMBER_NAME_CLAZZ;

	/**
	 * MemberName 中的 name 字段
	 */
	private static final Field FIELD_MEMBER_NAME_NAME;

	static {
		try {
			Class<?> classDirectMethodHandle = Class.forName("java.lang.invoke.DirectMethodHandle");
			FIELD_MEMBER_NAME = ReflectUtil.setAccessible(classDirectMethodHandle.getDeclaredField("member"));
			Class<?> classMemberName = Class.forName("java.lang.invoke.MemberName");
			FIELD_MEMBER_NAME_CLAZZ = ReflectUtil.setAccessible(classMemberName.getDeclaredField("clazz"));
			FIELD_MEMBER_NAME_NAME = ReflectUtil.setAccessible(classMemberName.getDeclaredField("name"));
		}
		catch (ClassNotFoundException | NoSuchFieldException e) {
			throw new LambdaBusinessException(e);
		}
	}

	/**
	 * Lambda 表达式所属的类
	 */
	private final Class<?> clazz;

	/**
	 * Lambda 表达式方法名
	 */
	private final String name;

	/**
	 * 构造函数
	 * @param func 代理对象，必须是通过 IDEA 调试环境生成的 Lambda 代理
	 * @throws LambdaBusinessException 如果无法从代理对象中提取元数据
	 */
	public IdeaProxyLambdaMeta(Proxy func) {
		InvocationHandler handler = Proxy.getInvocationHandler(func);
		try {
			Object dmh = ReflectUtil.setAccessible(handler.getClass().getDeclaredField("val$target")).get(handler);
			Object member = FIELD_MEMBER_NAME.get(dmh);
			clazz = (Class<?>) FIELD_MEMBER_NAME_CLAZZ.get(member);
			name = (String) FIELD_MEMBER_NAME_NAME.get(member);
		}
		catch (IllegalAccessException | NoSuchFieldException e) {
			throw new LambdaBusinessException(e);
		}
	}

	/**
	 * 获取 Lambda 表达式实现方法的名称
	 * @return Lambda 表达式对应的方法名
	 */
	@Override
	public String getImplMethodName() {
		return name;
	}

	/**
	 * 获取 Lambda 表达式实例化的类
	 * @return Lambda 表达式中方法所属的类
	 */
	@Override
	public Class<?> getInstantiatedClass() {
		return clazz;
	}

	/**
	 * 返回 Lambda 表达式的字符串表示 格式为：类名::方法名
	 * @return Lambda 表达式的字符串表示
	 */
	@Override
	public String toString() {
		return clazz.getSimpleName() + "::" + name;
	}

}

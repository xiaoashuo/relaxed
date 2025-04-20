package com.relaxed.common.core.util.lambda;

/**
 * Lambda表达式元数据接口。 定义了获取Lambda表达式相关信息的标准方法，包括： 1. 实现方法的名称 2. 实现类的Class对象
 *
 * 该接口的实现类包括： - IdeaProxyLambdaMeta: 处理IDEA环境下的Lambda表达式 - ReflectLambdaMeta:
 * 处理标准JDK的Lambda表达式 - ShadowLambdaMeta: 处理特殊场景下的Lambda表达式
 *
 * @author Yakir
 * @since 1.0
 */
public interface LambdaMeta {

	/**
	 * 获取Lambda表达式实现方法的名称 例如对于方法引用 Entity::getName，返回 "getName"
	 * @return Lambda表达式对应的实现方法名称
	 */
	String getImplMethodName();

	/**
	 * 获取Lambda表达式所在的类 例如对于方法引用 Entity::getName，返回 Entity.class
	 * @return Lambda表达式定义所在的类
	 */
	Class<?> getInstantiatedClass();

}

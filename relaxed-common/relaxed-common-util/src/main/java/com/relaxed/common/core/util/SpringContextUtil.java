package com.relaxed.common.core.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Spring 应用上下文工具类，提供对 Spring 容器中 Bean 和环境配置的访问功能。
 *
 * 主要功能： 1. 获取 Spring 容器中的 Bean 实例 2. 获取环境配置信息 3. 获取激活的配置文件 4. 提供类型安全的 Bean 获取方法
 *
 * 使用说明： - 该工具类会在 Spring 容器启动时自动初始化 - 所有方法都是线程安全的 - 在非 Spring 管理的类中也可以使用此工具类访问 Spring 容器 -
 * 建议优先使用依赖注入，仅在无法使用依赖注入的场景下使用此工具类
 *
 * @author Yakir
 * @since 1.0
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

	/**
	 * Spring 应用上下文
	 */
	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		SpringContextUtil.context = context;
	}

	/**
	 * 根据 Bean 名称获取 Bean 实例
	 * @param name Bean 的名称
	 * @param <T> Bean 的类型
	 * @return Bean 实例
	 * @throws BeansException 如果找不到 Bean 或类型转换失败
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) context.getBean(name);
	}

	/**
	 * 根据 Bean 类型获取 Bean 实例
	 * @param clazz Bean 的类型
	 * @param <T> Bean 的类型
	 * @return Bean 实例
	 * @throws BeansException 如果找不到 Bean 或存在多个同类型的 Bean
	 */
	public static <T> T getBean(Class<T> clazz) {
		return context.getBean(clazz);
	}

	/**
	 * 根据 Bean 名称和类型获取 Bean 实例
	 * @param name Bean 的名称
	 * @param clazz Bean 的类型
	 * @param <T> Bean 的类型
	 * @return Bean 实例
	 * @throws BeansException 如果找不到 Bean 或类型不匹配
	 */
	public static <T> T getBean(String name, Class<T> clazz) {
		return context.getBean(name, clazz);
	}

	/**
	 * 获取指定类型的所有 Bean 实例
	 * @param type Bean 的类型
	 * @param <T> Bean 的类型
	 * @return Bean 名称到实例的映射
	 */
	public static <T> Map<String, T> getBeansOfType(Class<T> type) {
		return context.getBeansOfType(type);
	}

	/**
	 * 获取指定类型的所有 Bean 名称
	 * @param type Bean 的类型
	 * @return Bean 名称数组
	 */
	public static String[] getBeanNamesForType(Class<?> type) {
		return context.getBeanNamesForType(type);
	}

	/**
	 * 获取配置属性值
	 * @param key 配置属性的键
	 * @return 配置属性的值，如果不存在则返回 null
	 */
	public static String getProperty(String key) {
		return context.getEnvironment().getProperty(key);
	}

	/**
	 * 获取当前激活的配置文件
	 * @return 激活的配置文件名称数组
	 */
	public static String[] getActiveProfiles() {
		return context.getEnvironment().getActiveProfiles();
	}

	/**
	 * 获取 Spring 环境配置对象
	 * @return Spring 环境配置对象
	 */
	public static Environment getEnvironment() {
		return context.getEnvironment();
	}

	/**
	 * 获取当前激活的配置文件
	 * @return 激活的配置文件名称数组
	 * @deprecated 请使用 {@link #getActiveProfiles()} 方法替代
	 */
	@Deprecated
	public static String[] getActiveProfile() {
		return getEnvironment().getActiveProfiles();
	}

	/**
	 * 获取 Spring 应用上下文
	 * @return Spring 应用上下文
	 */
	public static ApplicationContext getContext() {
		return context;
	}

}

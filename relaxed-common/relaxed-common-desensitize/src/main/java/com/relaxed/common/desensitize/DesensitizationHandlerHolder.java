package com.relaxed.common.desensitize;

import com.relaxed.common.desensitize.handler.DesensitizationHandler;
import com.relaxed.common.desensitize.handler.RegexDesensitizationHandler;
import com.relaxed.common.desensitize.handler.SimpleDesensitizationHandler;
import com.relaxed.common.desensitize.handler.SlideDesensitizationHandler;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 脱敏处理器持有者。 负责管理各种类型的脱敏处理器，包括： 1. 滑动脱敏处理器（SlideDesensitizationHandler） 2.
 * 正则脱敏处理器（RegexDesensitizationHandler） 3. 简单脱敏处理器（SimpleDesensitizationHandler）
 *
 * 简单脱敏处理器通过SPI机制加载，支持用户自定义扩展。
 *
 * @author Hccake
 * @since 1.0
 */
public final class DesensitizationHandlerHolder {

	private DesensitizationHandlerHolder() {
	}

	/**
	 * 处理器类型与实例的映射表
	 */
	private static final Map<Class<? extends DesensitizationHandler>, DesensitizationHandler> MAP = new ConcurrentHashMap<>();

	static {
		// 注册滑动脱敏处理器
		MAP.put(SlideDesensitizationHandler.class, new SlideDesensitizationHandler());
		// 注册正则脱敏处理器
		MAP.put(RegexDesensitizationHandler.class, new RegexDesensitizationHandler());
		// 通过SPI加载所有简单脱敏处理器
		ServiceLoader<SimpleDesensitizationHandler> loadedDrivers = ServiceLoader
				.load(SimpleDesensitizationHandler.class);
		for (SimpleDesensitizationHandler desensitizationHandler : loadedDrivers) {
			MAP.put(desensitizationHandler.getClass(), desensitizationHandler);
		}
	}

	/**
	 * 获取指定类型的脱敏处理器
	 * @param handlerClass 处理器类型
	 * @return 处理器实例
	 */
	public static DesensitizationHandler getHandler(Class<? extends DesensitizationHandler> handlerClass) {
		return MAP.get(handlerClass);
	}

	/**
	 * 获取正则脱敏处理器
	 * @return 正则脱敏处理器实例
	 */
	public static RegexDesensitizationHandler getRegexDesensitizationHandler() {
		return (RegexDesensitizationHandler) MAP.get(RegexDesensitizationHandler.class);
	}

	/**
	 * 获取滑动脱敏处理器
	 * @return 滑动脱敏处理器实例
	 */
	public static SlideDesensitizationHandler getSlideDesensitizationHandler() {
		return (SlideDesensitizationHandler) MAP.get(SlideDesensitizationHandler.class);
	}

	/**
	 * 获取指定类型的简单脱敏处理器
	 * @param handlerClass 处理器类型
	 * @return 简单脱敏处理器实例
	 */
	public static SimpleDesensitizationHandler getSimpleHandler(
			Class<? extends SimpleDesensitizationHandler> handlerClass) {
		return (SimpleDesensitizationHandler) MAP.get(handlerClass);
	}

	/**
	 * 注册脱敏处理器
	 * @param handlerClass 处理器类型
	 * @param handler 处理器实例
	 * @return 之前注册的处理器实例，如果之前未注册则返回null
	 */
	public static DesensitizationHandler addHandler(Class<? extends DesensitizationHandler> handlerClass,
			DesensitizationHandler handler) {
		return MAP.put(handlerClass, handler);
	}

}

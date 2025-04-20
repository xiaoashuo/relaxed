package com.relaxed.common.desensitize;

import cn.hutool.core.lang.Assert;
import com.relaxed.common.desensitize.enums.RegexDesensitizationTypeEnum;
import com.relaxed.common.desensitize.enums.SlideDesensitizationTypeEnum;
import com.relaxed.common.desensitize.functions.DesensitizeFunction;
import com.relaxed.common.desensitize.handler.RegexDesensitizationHandler;
import com.relaxed.common.desensitize.handler.SimpleDesensitizationHandler;
import com.relaxed.common.desensitize.handler.SlideDesensitizationHandler;
import com.relaxed.common.desensitize.json.annotation.JsonRegexDesensitize;
import com.relaxed.common.desensitize.json.annotation.JsonSimpleDesensitize;
import com.relaxed.common.desensitize.json.annotation.JsonSlideDesensitize;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 注解处理器持有者。 负责管理脱敏注解与处理函数的映射关系，支持以下脱敏类型： 1. 简单脱敏（Simple）：使用预定义的处理器处理 2.
 * 正则脱敏（Regex）：使用正则表达式匹配替换 3. 滑动脱敏（Slide）：根据左右边界滑动处理
 *
 * 提供了注解处理函数的注册、获取和查询功能。
 *
 * @author Yakir
 * @since 1.0
 */
public class AnnotationHandlerHolder {

	private AnnotationHandlerHolder() {
	}

	/**
	 * 注解类型与处理函数的映射表
	 */
	private static final Map<Class<? extends Annotation>, DesensitizeFunction> ANNOTATION_HANDLER = new HashMap<>();

	static {
		// 注册简单脱敏处理函数
		ANNOTATION_HANDLER.put(JsonSimpleDesensitize.class, (annotation, value) -> {
			JsonSimpleDesensitize an = (JsonSimpleDesensitize) annotation;
			Class<? extends SimpleDesensitizationHandler> handlerClass = an.handler();
			SimpleDesensitizationHandler desensitizationHandler = DesensitizationHandlerHolder
					.getSimpleHandler(handlerClass);
			Assert.notNull(desensitizationHandler, "SimpleDesensitizationHandler can not be Null");
			return desensitizationHandler.handle(value);
		});

		// 注册正则脱敏处理函数
		ANNOTATION_HANDLER.put(JsonRegexDesensitize.class, (annotation, value) -> {
			JsonRegexDesensitize an = (JsonRegexDesensitize) annotation;
			RegexDesensitizationTypeEnum type = an.type();
			RegexDesensitizationHandler regexDesensitizationHandler = DesensitizationHandlerHolder
					.getRegexDesensitizationHandler();
			return RegexDesensitizationTypeEnum.CUSTOM.equals(type)
					? regexDesensitizationHandler.handle(value, an.regex(), an.replacement())
					: regexDesensitizationHandler.handle(value, type);
		});

		// 注册滑动脱敏处理函数
		ANNOTATION_HANDLER.put(JsonSlideDesensitize.class, (annotation, value) -> {
			JsonSlideDesensitize an = (JsonSlideDesensitize) annotation;
			SlideDesensitizationTypeEnum type = an.type();
			SlideDesensitizationHandler slideDesensitizationHandler = DesensitizationHandlerHolder
					.getSlideDesensitizationHandler();
			return SlideDesensitizationTypeEnum.CUSTOM.equals(type) ? slideDesensitizationHandler.handle(value,
					an.leftPlainTextLen(), an.rightPlainTextLen(), an.maskString())
					: slideDesensitizationHandler.handle(value, type);
		});
	}

	/**
	 * 获取指定注解类型的处理函数
	 * @param annotationType 注解类型
	 * @return 脱敏处理函数，如果未找到则返回null
	 */
	public static DesensitizeFunction getHandleFunction(Class<? extends Annotation> annotationType) {
		return ANNOTATION_HANDLER.get(annotationType);
	}

	/**
	 * 注册注解处理函数
	 * @param annotationType 注解类型
	 * @param desensitizeFunction 脱敏处理函数
	 * @return 之前注册的处理函数，如果之前未注册则返回null
	 */
	public static DesensitizeFunction addHandleFunction(Class<? extends Annotation> annotationType,
			DesensitizeFunction desensitizeFunction) {
		Assert.notNull(annotationType, "annotation cannot be null");
		Assert.notNull(desensitizeFunction, "desensitization function cannot be null");
		return ANNOTATION_HANDLER.put(annotationType, desensitizeFunction);
	}

	/**
	 * 获取所有支持的注解类型
	 * @return 注解类型集合
	 */
	public static Set<Class<? extends Annotation>> getAnnotationClasses() {
		return ANNOTATION_HANDLER.keySet();
	}

}

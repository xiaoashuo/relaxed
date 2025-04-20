package com.relaxed.common.translation.core;

import com.relaxed.common.translation.enums.LangEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 翻译请求抽象基类。 提供了翻译请求的通用实现，包括： 1. 语言映射管理 2. 请求参数生成 3. 默认语言支持
 *
 * 子类需要实现具体的请求参数填充和请求执行逻辑。
 *
 * @param <P> 翻译参数类型
 * @author Yakir
 * @since 1.0
 */
public abstract class AbstractTranslationRequest<P extends TranslationParam> implements TranslationRequest {

	/**
	 * 语言映射表，用于存储语言枚举和实际语言代码的对应关系
	 */
	protected Map<LangEnum, String> langMap = new HashMap<>();

	/**
	 * 表单数据，用于存储请求参数
	 */
	protected Map<String, String> formData = new HashMap<>();

	/**
	 * 翻译参数
	 */
	protected P translationParam;

	/**
	 * 设置翻译参数
	 * @param translationParam 翻译参数
	 */
	public void setTranslationParam(P translationParam) {
		this.translationParam = translationParam;
	}

	/**
	 * 构造函数，初始化默认配置
	 */
	protected AbstractTranslationRequest() {
		init();
	}

	/**
	 * 初始化操作 子类可以重写此方法添加自定义初始化逻辑
	 */
	protected void init() {
		initDefaultLangSupport();
	}

	@Override
	public String generateParam() {
		return put(fillFormData(formData));
	}

	/**
	 * 填充表单数据 子类可以重写此方法自定义表单数据填充逻辑
	 * @param formData 表单数据
	 * @return 填充后的表单数据
	 */
	protected Map<String, String> fillFormData(Map<String, String> formData) {
		return formData;
	}

	/**
	 * 处理表单数据 子类需要实现此方法，将表单数据转换为请求参数字符串
	 * @param formData 表单数据
	 * @return 请求参数字符串
	 */
	public abstract String put(Map<String, String> formData);

	/**
	 * 初始化默认语言支持 添加常用的语言映射关系
	 */
	protected void initDefaultLangSupport() {
		langMap.put(LangEnum.ZH, "zh-CN");
		langMap.put(LangEnum.EN, "en");
		langMap.put(LangEnum.JP, "ja");
		langMap.put(LangEnum.KOR, "ko");
		langMap.put(LangEnum.FRA, "fr");
		langMap.put(LangEnum.RU, "ru");
		langMap.put(LangEnum.DE, "de");
	}

	/**
	 * 添加语言支持
	 * @param source 语言枚举
	 * @param val 实际语言代码
	 */
	protected void addLangSupport(LangEnum source, String val) {
		langMap.put(source, val);
	}

	/**
	 * 移除语言支持
	 * @param source 语言枚举
	 */
	protected void delLangSupport(LangEnum source) {
		langMap.remove(source);
	}

}

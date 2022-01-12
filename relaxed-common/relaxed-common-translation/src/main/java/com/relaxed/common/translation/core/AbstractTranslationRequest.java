package com.relaxed.common.translation.core;

import com.relaxed.common.translation.enums.LangEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yakir
 * @Topic AbstractHttpParam
 * @Description
 * @date 2022/1/11 16:17
 * @Version 1.0
 */
public abstract class AbstractTranslationRequest<P extends TranslationParam> implements TranslationRequest {

	protected Map<LangEnum, String> langMap = new HashMap<>();

	protected Map<String, String> formData = new HashMap<>();

	protected P translationParam;

	public void setTranslationParam(P translationParam) {
		this.translationParam = translationParam;
	}

	protected AbstractTranslationRequest() {
		init();
	}

	/**
	 * 做一些初始化操作
	 */
	protected void init() {
		initDefaultLangSupport();
	}

	@Override
	public String generateParam() {
		return put(fillFormData(formData));
	}

	protected Map<String, String> fillFormData(Map<String, String> formData) {
		return formData;
	}

	public abstract String put(Map<String, String> formData);

	/**
	 * 初始化默认语言支持
	 */
	protected void initDefaultLangSupport() {
		langMap.put(LangEnum.ZH, "zh-CN");
		langMap.put(LangEnum.EN, "en");
		langMap.put(LangEnum.JP, "ja");
		langMap.put(LangEnum.KOR, "ko");
		langMap.put(LangEnum.FRA, "fr");
		langMap.put(LangEnum.RU, "ru");
		langMap.put(LangEnum.DE, "de");
	};

	protected void addLangSupport(LangEnum source, String val) {
		langMap.put(source, val);
	}

	protected void delLangSupport(LangEnum source) {
		langMap.remove(source);
	}

}

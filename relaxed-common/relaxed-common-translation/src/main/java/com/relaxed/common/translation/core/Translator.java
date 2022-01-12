package com.relaxed.common.translation.core;

/**
 * @author Yakir
 * @Topic Translator
 * @Description 翻译者
 * @date 2022/1/12 9:41
 * @Version 1.0
 */
public class Translator {



   /**
    * 翻译
    * @author yakir
    * @date 2022/1/12 14:50
    * @param request  接收请求
    * @return com.relaxed.common.translation.core.TranslationResponse<R>
    */
	public <T extends TranslationRequest, R> TranslationResponse<R> translate(T request) {
		String param = request.generateParam();
		return request.execute(param);
	}

}

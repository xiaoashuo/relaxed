package com.relaxed.common.translation.core;

/**
 * 翻译执行器。 负责执行翻译请求，处理请求参数生成和响应结果封装。 提供了统一的翻译请求执行入口。
 *
 * @author Yakir
 * @since 1.0
 */
public class Translator {

	/**
	 * 执行翻译请求
	 * @param request 翻译请求对象
	 * @param <T> 请求类型
	 * @param <R> 响应数据类型
	 * @return 翻译响应结果
	 */
	public <T extends TranslationRequest, R> TranslationResponse<R> translate(T request) {
		String param = request.generateParam();
		return request.execute(param);
	}

}

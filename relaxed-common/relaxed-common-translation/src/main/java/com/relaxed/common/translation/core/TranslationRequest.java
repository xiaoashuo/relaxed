package com.relaxed.common.translation.core;

import com.relaxed.common.model.result.R;
import com.relaxed.common.translation.enums.TransEnum;

/**
 * 翻译请求接口。 定义了翻译服务请求的基本行为，包括获取请求URL、生成请求参数、获取翻译类型和执行请求。 实现类需要提供具体的翻译服务实现，如Google翻译、百度翻译等。
 *
 * @author Yakir
 * @since 1.0
 */
public interface TranslationRequest {

	/**
	 * 获取翻译服务的请求URL
	 * @return 翻译服务的请求URL
	 */
	String getUrl();

	/**
	 * 生成请求参数 根据翻译参数生成符合翻译服务要求的请求参数
	 * @return 请求参数字符串
	 */
	String generateParam();

	/**
	 * 获取翻译类型 用于标识当前请求的翻译服务类型
	 * @return 翻译类型枚举
	 */
	TransEnum tsType();

	/**
	 * 执行翻译请求
	 * @param param 请求参数
	 * @param <T> 响应数据类型
	 * @return 翻译响应结果
	 */
	<T> TranslationResponse<T> execute(String param);

}

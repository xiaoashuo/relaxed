package com.relaxed.common.translation.core;

import com.relaxed.common.model.result.R;
import com.relaxed.common.translation.enums.TransEnum;

/**
 * @author Yakir
 * @Topic HttpParam
 * @Description
 * @date 2022/1/11 16:15
 * @Version 1.0
 */
public interface TranslationRequest {

	/**
	 * 获取请求url
	 * @return
	 */
	String getUrl();

	/**
	 * 生成请求参数
	 * @return
	 */
	String generateParam();

	/**
	 * 翻译类型
	 * @return
	 */
	TransEnum tsType();
	/**
	 * 执行请求
	 * @author yakir
	 * @date 2022/1/12 9:45
	 * @param param
	 * @return TranslationResponse<T>
	 */
	<T> TranslationResponse<T> execute(String param);

}

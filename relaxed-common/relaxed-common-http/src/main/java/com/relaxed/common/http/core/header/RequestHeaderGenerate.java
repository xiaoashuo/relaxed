package com.relaxed.common.http.core.header;

import com.relaxed.common.http.core.request.IRequest;
import com.relaxed.common.http.domain.RequestForm;

import java.util.Map;

/**
 * @author Yakir
 * @Topic RequestHeaderGenerate
 * @Description 请求头生成器
 * @date 2022/5/23 9:00
 * @Version 1.0
 */
public interface RequestHeaderGenerate {

	/**
	 * 头生成器
	 * @author yakir
	 * @date 2022/5/23 9:04
	 * @param url 请求地址
	 * @param requestForm 请求参数
	 * @return java.util.Map<java.lang.String, java.lang.String> 返回值
	 */
	Map<String, String> generate(String url, RequestForm requestForm);

}

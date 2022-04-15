package com.relaxed.common.core.util.http;

import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.io.resource.StringResource;
import cn.hutool.http.HttpResource;

public class HttpUtil extends cn.hutool.http.HttpUtil {

	/**
	 * 构建multipart/form 多组成表单参数 默认根据body获取content-type
	 * @author yakir
	 * @date 2022/4/15 9:20
	 * @param value
	 * @return cn.hutool.core.io.resource.Resource
	 */
	public static Resource buildMultipartFormWithType(String value) {
		return buildMultipartFormWithType(value, getContentTypeByRequestBody(value));
	}

	/**
	 * 构建multipart/form 多组成表单参数 可以指定附件类型
	 * @author yakir
	 * @date 2022/4/15 9:11
	 * @param value 字符值
	 * @param contentType 类型 eg: application/json
	 * @return cn.hutool.core.io.resource.Resource
	 */
	public static Resource buildMultipartFormWithType(String value, String contentType) {
		StringResource stringResource = new StringResource(value);
		return new HttpResource(stringResource, contentType);
	}

}

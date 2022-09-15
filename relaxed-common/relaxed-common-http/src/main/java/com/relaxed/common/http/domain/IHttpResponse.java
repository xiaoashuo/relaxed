package com.relaxed.common.http.domain;

import java.util.List;
import java.util.Map;

/**
 * @author Yakir
 * @Topic IHttpResponse
 * @Description
 * @date 2022/5/18 17:49
 * @Version 1.0
 */
public interface IHttpResponse {

	/**
	 * 获取charset
	 * @author yakir
	 * @date 2022/5/18 17:49
	 * @return java.lang.String
	 */
	String getCharset();

	/**
	 * 获取响应字节
	 * @author yakir
	 * @date 2022/5/18 17:49
	 * @return byte[]
	 */
	byte[] bodyBytes();

	/**
	 * 获取响应字符串
	 * @author yakir
	 * @date 2022/5/18 17:50
	 * @return java.lang.String
	 */
	String body();

	/**
	 * 响应头
	 * @return
	 */
	Map<String, List<String>> headers();

}

package com.relaxed.common.http.core.request;

import com.relaxed.common.http.core.response.IResponse;
import com.relaxed.common.http.domain.RequestForm;
import com.relaxed.common.http.domain.ResponseWrapper;

/**
 * @author Yakir
 * @Topic IRequest
 * @Description
 * @date 2022/4/24 15:41
 * @Version 1.0
 */
public interface IRequest<R extends IResponse> {

	/**
	 * request url
	 * @author yakir
	 * @date 2021/8/2 10:01
	 * @return java.lang.String
	 */
	String getUrl(String baseUrl);

	/**
	 * 来自渠道
	 * @author yakir
	 * @date 2022/2/8 9:26
	 * @return java.lang.String
	 */
	String getChannel();

	/**
	 * 生成请求参数
	 * @author yakir
	 * @date 2022/4/24 15:53
	 * @return java.lang.String
	 */
	RequestForm generateRequestParam();

	/**
	 * 是否为下载请求
	 * @author yakir
	 * @date 2022/5/18 13:20
	 * @return boolean
	 */
	default boolean isDownloadRequest() {
		return false;
	}

	/**
	 * 转换成响应对象
	 * @author yakir
	 * @date 2022/4/24 15:53
	 * @param response
	 * @return R
	 */
	R convertToResponse(ResponseWrapper response);

}

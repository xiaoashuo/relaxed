package com.relaxed.common.http.core;

import com.relaxed.common.http.core.request.IRequest;
import com.relaxed.common.http.core.response.IResponse;

/**
 * @author Yakir
 * @Topic ISender
 * @Description
 * @date 2022/5/18 8:33
 * @Version 1.0
 */
public interface ISender {

	/**
	 * 发送请求
	 * @author yakir
	 * @date 2022/5/18 8:34
	 * @param request
	 * @return R
	 */
	<R extends IResponse> R send(IRequest<R> request);

}

package com.relaxed.common.http.core;

import com.relaxed.common.http.core.request.IRequest;
import com.relaxed.common.http.core.response.IResponse;
import com.relaxed.common.model.result.R;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yakir
 * @Topic ISender
 * @Description
 * @date 2022/5/18 8:33
 * @Version 1.0
 */
public interface ISender<C, CR> {

	/**
	 * 发送请求
	 * @author yakir
	 * @date 2022/5/18 8:34
	 * @param request
	 * @return R
	 */
	default <R extends IResponse> R send(IRequest<R> request) {
		return send(request, null);
	}

	<R extends IResponse> R send(IRequest<R> request, Map<String, String> headerMap);

}

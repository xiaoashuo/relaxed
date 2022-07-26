package com.relaxed.common.http.core.interceptor;

import com.relaxed.common.http.domain.RequestForm;
import com.relaxed.common.model.result.R;

import java.util.Map;

/**
 * @author Yakir
 * @Topic RequestInterceptor
 * @Description
 * @date 2022/7/26 17:53
 * @Version 1.0
 */
public interface RequestInterceptor<C, R> {

	/**
	 * 拦截器
	 * @param request 请求客户端
	 * @param requestForm 请求表单
	 * @param context 请求上下文
	 * @return 请求客户端
	 */
	C requestInterceptor(C request, RequestForm requestForm, Map<String, Object> context);

	/**
	 * 拦截响应
	 * @param request 请求客户端
	 * @param response 响应客户端
	 * @param context 请求上下文
	 * @return 响应客户端
	 */
	R responseInterceptor(C request, R response, Map<String, Object> context);

}

package com.relaxed.common.secret.handler;

import cn.hutool.core.util.ClassUtil;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

/**
 * @author Yakir
 * @Topic SecretHandler
 * @Description
 * @date 2021/11/15 10:15
 * @Version 1.0
 */
public interface SecretHandler<T, R> {

	/**
	 * 加密响应
	 * @author yakir
	 * @date 2021/11/15 10:27
	 * @param body
	 * @param request
	 * @param response
	 * @return java.lang.Object
	 */
	Object encryptResBody(R body, ServerHttpRequest request, ServerHttpResponse response);

	/**
	 * 解密请求
	 * @author yakir
	 * @date 2021/11/15 15:51
	 * @param body
	 * @return T
	 */
	T decryptReqBody(T body);

	/**
	 * 消息转换器之前处理参数解密
	 * @author yakir
	 * @date 2021/11/15 16:09
	 * @param body
	 * @return java.lang.String
	 */
	default String decryptReqBody(String body) {
		return body;
	}

	/**
	 * 得到参数化类型
	 * @author yakir
	 * @date 2021/11/15 10:39
	 * @param parameterizedIndex
	 * @return java.lang.Class<?>
	 */
	default Class<?> getParameterizedType(Integer parameterizedIndex) {
		return ClassUtil.getTypeArgument(getClass(), parameterizedIndex);
	}

	/**
	 * 是否支持请求处理类型
	 * @author yakir
	 * @date 2021/11/15 10:40
	 * @param clazz
	 * @return boolean
	 */
	default boolean supportReqType(Class<?> clazz) {
		return getParameterizedType(0).isAssignableFrom(clazz);
	}

	/**
	 * 是否支持响应处理
	 * @author yakir
	 * @date 2021/11/15 17:13
	 * @param clazz
	 * @return boolean
	 */
	default boolean supportResType(Class<?> clazz) {
		return getParameterizedType(1).isAssignableFrom(clazz);
	}

}

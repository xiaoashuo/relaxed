package com.relaxed.common.secret.handler;

import cn.hutool.core.util.ClassUtil;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

/**
 * @author Yakir
 * @Topic SecretHandler
 * @Description
 * @date 2022/9/5 13:40
 * @Version 1.0
 */
public interface SecretHandler<T, R> {

	/**
	 * 是否支持请求处理类型
	 * @author yakir
	 * @date 2021/11/15 10:40
	 * @param clazz
	 * @return boolean
	 */
	default boolean supportReqType(Class<?> clazz) {
		return ClassUtil.getTypeArgument(getClass(), 0).isAssignableFrom(clazz);
	}

	/**
	 * 消息转换器之前处理参数解密
	 * @date 2022/9/5 11:21
	 * @param content 原始内容 参数
	 * @return java.lang.String 转换后 内容参数
	 */
	default String beforeBodyRead(String content) {
		return content;
	}

	/**
	 * 消息转换器之后处理参数
	 * @date 2022/9/5 11:23
	 * @param body
	 * @return T 转换后参数
	 */
	default T afterBodyRead(T body) {
		return body;
	}

	/**
	 * 是否支持响应处理
	 * @author yakir
	 * @date 2021/11/15 17:13
	 * @param clazz
	 * @return boolean
	 */
	default boolean supportResType(Class<?> clazz) {
		return ClassUtil.getTypeArgument(getClass(), 1).isAssignableFrom(clazz);
	}

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

}

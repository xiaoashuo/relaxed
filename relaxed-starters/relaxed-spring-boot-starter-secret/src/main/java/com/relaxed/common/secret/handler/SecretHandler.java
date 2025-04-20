package com.relaxed.common.secret.handler;

import cn.hutool.core.util.ClassUtil;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

/**
 * 加密处理器接口 定义请求解密和响应加密的核心处理方法 支持在消息转换器前后进行加解密处理
 *
 * @param <T> 请求体类型
 * @param <R> 响应体类型
 * @author Yakir
 * @since 1.0
 */
public interface SecretHandler<T, R> {

	/**
	 * 检查是否支持指定的请求处理类型 通过泛型参数类型判断是否支持处理指定的类
	 * @param clazz 需要检查的类
	 * @return 如果支持处理返回true，否则返回false
	 */
	default boolean supportReqType(Class<?> clazz) {
		return ClassUtil.getTypeArgument(getClass(), 0).isAssignableFrom(clazz);
	}

	/**
	 * 在消息转换器之前处理参数解密 用于处理原始请求内容的解密
	 * @param content 原始请求内容
	 * @return 解密后的内容
	 */
	default String beforeBodyRead(String content) {
		return content;
	}

	/**
	 * 在消息转换器之后处理参数 用于处理已转换的请求体的解密
	 * @param body 已转换的请求体
	 * @return 解密后的请求体
	 */
	default T afterBodyRead(T body) {
		return body;
	}

	/**
	 * 检查是否支持指定的响应处理类型 通过泛型参数类型判断是否支持处理指定的类
	 * @param clazz 需要检查的类
	 * @return 如果支持处理返回true，否则返回false
	 */
	default boolean supportResType(Class<?> clazz) {
		return ClassUtil.getTypeArgument(getClass(), 1).isAssignableFrom(clazz);
	}

	/**
	 * 加密响应体 对响应体进行加密处理
	 * @param body 需要加密的响应体
	 * @param request HTTP请求对象
	 * @param response HTTP响应对象
	 * @return 加密后的响应体
	 */
	Object encryptResBody(R body, ServerHttpRequest request, ServerHttpResponse response);

}

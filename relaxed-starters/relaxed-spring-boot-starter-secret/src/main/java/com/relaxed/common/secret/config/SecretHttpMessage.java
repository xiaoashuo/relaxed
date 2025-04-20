package com.relaxed.common.secret.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.InputStream;

/**
 * 加密HTTP消息类 用于处理加密的HTTP请求消息 实现了HttpInputMessage接口，封装了加密后的请求体和HTTP头信息
 *
 * @author Yakir
 * @since 1.0
 */
@AllArgsConstructor
@Data
public class SecretHttpMessage implements HttpInputMessage {

	/**
	 * 加密后的请求体输入流
	 */
	private InputStream body;

	/**
	 * HTTP请求头信息
	 */
	private HttpHeaders httpHeaders;

	/**
	 * 获取加密后的请求体输入流
	 * @return 加密后的请求体输入流
	 */
	@Override
	public InputStream getBody() {
		return this.body;
	}

	/**
	 * 获取HTTP请求头信息
	 * @return HTTP请求头信息
	 */
	@Override
	public HttpHeaders getHeaders() {
		return this.httpHeaders;
	}

}

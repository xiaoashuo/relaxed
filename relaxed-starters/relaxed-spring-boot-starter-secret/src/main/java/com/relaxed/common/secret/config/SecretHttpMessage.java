package com.relaxed.common.secret.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.InputStream;

/**
 * @author Yakir
 * @Topic SecretHttpMessage
 * @Description
 * @date 2021/11/15 15:10
 * @Version 1.0
 */
@AllArgsConstructor
@Data
public class SecretHttpMessage implements HttpInputMessage {

	private InputStream body;

	private HttpHeaders httpHeaders;

	@Override
	public InputStream getBody() {
		return this.body;
	}

	@Override
	public HttpHeaders getHeaders() {
		return this.httpHeaders;
	}

}

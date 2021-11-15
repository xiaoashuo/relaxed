package com.relaxed.common.secret.test;

import com.relaxed.common.secret.handler.SecretHandler;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;

/**
 * @author Yakir
 * @Topic DefaultParamHandler
 * @Description
 * @date 2021/11/15 10:42
 * @Version 1.0
 */
@Component
public class DefaultParamHandler implements SecretHandler<BaseDto, BaseDto> {

	@Override
	public BaseDto encryptResBody(BaseDto body, ServerHttpRequest request, ServerHttpResponse response) {
		body.setContent("加密数据");
		return body;
	}

	@Override
	public BaseDto decryptReqBody(BaseDto body) {
		body.setTest("ada");
		return body;
	}

}

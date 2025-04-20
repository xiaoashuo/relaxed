package com.relaxed.oauth2.auth.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.relaxed.common.model.result.R;
import com.relaxed.common.model.result.SysResultCode;

import java.io.IOException;

/**
 * OAuth2异常JSON序列化器 用于自定义OAuth2异常的JSON序列化格式 将OAuth2异常转换为统一的响应格式
 *
 * @author Yakir
 * @since 1.0
 */
public class CustomOAuth2ExceptionJackson2Serializer extends StdSerializer<CustomOAuth2Exception> {

	/**
	 * 默认构造函数 指定序列化的异常类型
	 */
	public CustomOAuth2ExceptionJackson2Serializer() {
		super(CustomOAuth2Exception.class);
	}

	/**
	 * 序列化OAuth2异常 将异常转换为统一的响应格式
	 * @param value 要序列化的异常
	 * @param jsonGenerator JSON生成器
	 * @param provider 序列化提供者
	 * @throws IOException 当序列化过程中发生IO错误时抛出
	 */
	@Override
	public void serialize(CustomOAuth2Exception value, JsonGenerator jsonGenerator, SerializerProvider provider)
			throws IOException {
		jsonGenerator.writeObject(R.failed(SysResultCode.UNAUTHORIZED, value.getMessage()));
	}

}

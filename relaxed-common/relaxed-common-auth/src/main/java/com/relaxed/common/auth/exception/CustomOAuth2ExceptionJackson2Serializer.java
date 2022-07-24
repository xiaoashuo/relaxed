package com.relaxed.common.auth.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.relaxed.common.model.result.R;
import com.relaxed.common.model.result.SysResultCode;

import java.io.IOException;

/**
 * @author Yakir
 * @Topic CustomOAuth2ExceptionJackson2Serializer
 * @Description
 * @date 2022/7/22 15:31
 * @Version 1.0
 */
public class CustomOAuth2ExceptionJackson2Serializer extends StdSerializer<CustomOAuth2Exception> {

	public CustomOAuth2ExceptionJackson2Serializer() {
		super(CustomOAuth2Exception.class);
	}

	@Override
	public void serialize(CustomOAuth2Exception value, JsonGenerator jsonGenerator, SerializerProvider provider)
			throws IOException {
		jsonGenerator.writeObject(R.failed(SysResultCode.UNAUTHORIZED, value.getMessage()));
	}

}

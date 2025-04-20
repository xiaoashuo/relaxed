package com.relaxed.common.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * 空Map序列化器，将null值序列化为空对象{}
 *
 * @author Hccake
 * @since 1.0.0
 */
public class NullMapJsonSerializer extends JsonSerializer<Object> {

	@Override
	public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
		if (value == null) {
			jsonGenerator.writeStartObject();
			jsonGenerator.writeEndObject();
		}
		else {
			jsonGenerator.writeObject(value);
		}
	}

}

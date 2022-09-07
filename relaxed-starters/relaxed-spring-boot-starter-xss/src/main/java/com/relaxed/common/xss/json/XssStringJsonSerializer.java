package com.relaxed.common.xss.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.relaxed.common.core.util.WebUtils;
import com.relaxed.common.xss.config.XssProperties;
import com.relaxed.common.xss.toolkit.HtmlKit;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

/**
 * @author Yakir
 * @Topic XssStringJsonSerializer
 * @Description
 * @date 2021/6/26 14:38
 * @Version 1.0
 */
@RequiredArgsConstructor
public class XssStringJsonSerializer extends JsonSerializer<String> {

	private final XssProperties xssProperties;

	@Override
	public Class<String> handledType() {
		return String.class;
	}

	@Override
	public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException {
		if (value != null) {
			if (!xssProperties.shouldNotFilter(WebUtils.getRequest())) {
				value = HtmlKit.cleanUnSafe(value);
			}
			jsonGenerator.writeString(value);
		}
	}

}

package com.relaxed.common.xss.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.relaxed.common.core.util.ServletUtils;
import com.relaxed.common.xss.config.XssProperties;
import com.relaxed.common.xss.toolkit.HtmlKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author Yakir
 * @Topic XssStringJsonDeserializer
 * @Description
 * @date 2021/6/26 14:38
 * @Version 1.0
 */
@RequiredArgsConstructor
@Slf4j
public class XssStringJsonDeserializer extends JsonDeserializer<String> {

	private final XssProperties xssProperties;

	@Override
	public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		String value = p.getValueAsString();
		if (xssProperties.shouldNotFilter(ServletUtils.getRequest())) {
			return value;
		}
		return value != null ? HtmlKit.cleanUnSafe(value) : null;
	}

	@Override
	public Class<String> handledType() {
		return String.class;
	}

}

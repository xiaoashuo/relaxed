package com.relaxed.common.xss.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.relaxed.common.core.util.WebUtils;
import com.relaxed.common.xss.config.XssProperties;
import com.relaxed.common.xss.toolkit.HtmlKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * XSS字符串JSON反序列化器 用于在JSON反序列化时对字符串进行XSS过滤 继承自JsonDeserializer，专门处理String类型的反序列化
 *
 * @author Yakir
 * @since 1.0
 */
@RequiredArgsConstructor
@Slf4j
public class XssStringJsonDeserializer extends JsonDeserializer<String> {

	private final XssProperties xssProperties;

	/**
	 * 反序列化字符串，并进行XSS过滤
	 * @param p JSON解析器
	 * @param ctxt 反序列化上下文
	 * @return 过滤后的字符串
	 * @throws IOException 如果反序列化过程中发生IO异常
	 */
	@Override
	public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		String value = p.getValueAsString();
		if (xssProperties.shouldNotFilter(WebUtils.getRequest())) {
			return value;
		}
		return value != null ? HtmlKit.cleanUnSafe(value) : null;
	}

	/**
	 * 获取处理的类型
	 * @return String.class
	 */
	@Override
	public Class<String> handledType() {
		return String.class;
	}

}

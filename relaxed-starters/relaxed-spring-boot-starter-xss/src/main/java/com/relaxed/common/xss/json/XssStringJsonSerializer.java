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
 * XSS字符串JSON序列化器 用于在JSON序列化时对字符串进行XSS过滤 继承自{@link JsonSerializer}，专门处理String类型的序列化
 *
 * @author Yakir
 * @since 1.0
 */
@RequiredArgsConstructor
public class XssStringJsonSerializer extends JsonSerializer<String> {

	private final XssProperties xssProperties;

	/**
	 * 获取处理的类型
	 * @return String.class
	 */
	@Override
	public Class<String> handledType() {
		return String.class;
	}

	/**
	 * 序列化字符串，并进行XSS过滤
	 * @param value 要序列化的字符串
	 * @param jsonGenerator JSON生成器
	 * @param serializerProvider 序列化提供者
	 * @throws IOException 如果序列化过程中发生IO异常
	 */
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

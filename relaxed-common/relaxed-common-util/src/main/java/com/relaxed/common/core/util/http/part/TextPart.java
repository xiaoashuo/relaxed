package com.relaxed.common.core.util.http.part;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.CharsetUtil;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TextPart implements Part {

	/**
	 * 字段值
	 */
	private String value;

	/**
	 * 内容类型
	 */
	private String contentType;

	/**
	 * 字符集
	 */
	private final Charset charset;

	public TextPart(String value) {
		this(value, null);
	}

	public TextPart(String value, String contentType) {
		this(value, contentType, CharsetUtil.CHARSET_UTF_8);
	}

	public TextPart(String value, String contentType, Charset charset) {
		this.value = value;
		this.contentType = contentType;
		this.charset = charset;
	}

	@Override
	public ByteArrayInputStream getStream() {
		return new ByteArrayInputStream(value.getBytes(charset));
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	public String toString() {
		return this.value;
	}

}

package com.relaxed.common.http.core.resource;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.ByteUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;

/**
 * @author Yakir
 * @Topic StringResource
 * @Description
 * @date 2022/5/23 15:13
 * @Version 1.0
 */
@Data
public class StringResource implements Resource {

	private String name;

	private String contentType;

	private String charset;

	private String data;

	public StringResource(String name, String data) {
		this(name, null, null, data);
	}

	public StringResource(String name, String contentType, String charset, String data) {
		this.name = name;
		this.contentType = contentType;
		this.charset = charset;
		this.data = data;
	}

	@Override
	public String getFileName() {
		return getName();
	}

	@Override
	public InputStream getStream() {
		return new ByteArrayInputStream(readBytes());
	}

	public byte[] readBytes() throws IORuntimeException {
		return StrUtil.bytes(this.data, this.charset);
	}

}

package com.relaxed.common.http.domain;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import lombok.Data;

import java.io.InputStream;

/**
 * @author Yakir
 * @Topic HttpResponse
 * @Description
 * @date 2022/5/18 17:20
 * @Version 1.0
 */
@Data
public class HttpResponseWrapper implements IHttpResponse {

	private String charset;

	/**
	 * 响应字节
	 */
	private byte[] bodyBytes;

	@Override
	public String getCharset() {
		return charset;
	}

	@Override
	public String body() {
		return HttpUtil.getString(bodyBytes, CharsetUtil.charset(charset), charset == null);
	}

	@Override
	public byte[] bodyBytes() {
		return bodyBytes;
	}

}
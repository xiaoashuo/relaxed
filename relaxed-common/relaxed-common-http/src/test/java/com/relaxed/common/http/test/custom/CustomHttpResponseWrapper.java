package com.relaxed.common.http.test.custom;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import com.relaxed.common.http.domain.IHttpResponse;
import lombok.Data;

/**
 * @author Yakir
 * @Topic HttpResponse
 * @Description
 * @date 2022/5/18 17:20
 * @Version 1.0
 */
@Data
public class CustomHttpResponseWrapper implements IHttpResponse {

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

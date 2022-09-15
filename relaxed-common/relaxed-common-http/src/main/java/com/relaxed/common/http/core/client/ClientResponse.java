package com.relaxed.common.http.core.client;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.cookie.GlobalCookieManager;

import java.io.Closeable;
import java.io.IOException;
import java.net.HttpCookie;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author Yakir
 * @Topic ClientResponse
 * @Description
 * @date 2022/9/15 11:08
 * @Version 1.0
 */
public class ClientResponse extends ClientBase<ClientResponse> {

	/**
	 * 从响应中获取的编码
	 */
	private Charset charsetFromResponse;

	// ---------------------------------------------------------------- Http Response
	// Header start

	/**
	 * 获取内容编码
	 * @return String
	 */
	public String contentEncoding() {
		return header(Header.CONTENT_ENCODING);
	}

	/**
	 * 获取内容长度，以下情况长度无效：
	 * <ul>
	 * <li>Transfer-Encoding: Chunked</li>
	 * <li>Content-Encoding: XXX</li>
	 * </ul>
	 * 参考：https://blog.csdn.net/jiang7701037/article/details/86304302
	 * @return 长度，-1表示服务端未返回或长度无效
	 * @since 5.7.9
	 */
	public long contentLength() {
		long contentLength = Convert.toLong(header(Header.CONTENT_LENGTH), -1L);
		if (contentLength > 0 && (isChunked() || StrUtil.isNotBlank(contentEncoding()))) {
			// 按照HTTP协议规范，在 Transfer-Encoding和Content-Encoding设置后 Content-Length 无效。
			contentLength = -1;
		}
		return contentLength;
	}

	/**
	 * 是否为gzip压缩过的内容
	 * @return 是否为gzip压缩过的内容
	 */
	public boolean isGzip() {
		final String contentEncoding = contentEncoding();
		return "gzip".equalsIgnoreCase(contentEncoding);
	}

	/**
	 * 是否为zlib(Deflate)压缩过的内容
	 * @return 是否为zlib(Deflate)压缩过的内容
	 * @since 4.5.7
	 */
	public boolean isDeflate() {
		final String contentEncoding = contentEncoding();
		return "deflate".equalsIgnoreCase(contentEncoding);
	}

	/**
	 * 是否为Transfer-Encoding:Chunked的内容
	 * @return 是否为Transfer-Encoding:Chunked的内容
	 * @since 4.6.2
	 */
	public boolean isChunked() {
		final String transferEncoding = header(Header.TRANSFER_ENCODING);
		return "Chunked".equalsIgnoreCase(transferEncoding);
	}

	/**
	 * 获取本次请求服务器返回的Cookie信息
	 * @return Cookie字符串
	 * @since 3.1.1
	 */
	public String getCookieStr() {
		return header(Header.SET_COOKIE);
	}

	// ---------------------------------------------------------------- Http Response
	// Header end

	/**
	 * 获取响应流字节码<br>
	 * 此方法会转为同步模式
	 * @return byte[]
	 */
	public byte[] bodyBytes() {
		return this.bodyBytes;
	}

	/**
	 * 获取响应主体
	 * @return String
	 * @throws HttpException 包装IO异常
	 */
	public String body() {
		return HttpUtil.getString(bodyBytes(), this.charset, null == this.charsetFromResponse);
	}

	/**
	 * 从Content-Disposition头中获取文件名
	 * @return 文件名，empty表示无
	 */
	private String getFileNameFromDisposition() {
		String fileName = null;
		final String disposition = header(Header.CONTENT_DISPOSITION);
		if (StrUtil.isNotBlank(disposition)) {
			fileName = ReUtil.get("filename=\"(.*?)\"", disposition, 1);
			if (StrUtil.isBlank(fileName)) {
				fileName = StrUtil.subAfter(disposition, "filename=", true);
			}
		}
		return fileName;
	}

}

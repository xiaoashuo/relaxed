package com.relaxed.common.core.util.http.part;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Part {

	/**
	 * 获取ContentType类型
	 * @return
	 */
	String getContentType();

	/**
	 * 获取流内容
	 * @return
	 */
	InputStream getStream();

	/**
	 * 将资源内容写出到流，不关闭输出流，但是关闭资源流
	 * @param out 输出流
	 * @throws IORuntimeException IO异常
	 * @since 5.3.5
	 */
	default void writeTo(OutputStream out) throws IORuntimeException {
		try (InputStream in = getStream()) {
			IoUtil.copy(in, out);
		}
		catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

}

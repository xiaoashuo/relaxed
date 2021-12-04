package com.relaxed.common.oss.s3.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Yakir
 * @Topic StreamMeta
 * @Description
 * @date 2021/11/26 15:26
 * @Version 1.0
 */
@Getter
@RequiredArgsConstructor
public class StreamMeta {

	private final Long size;

	private final InputStream inputStream;

	/**
	 * 转换为字节流模板
	 * @author yakir
	 * @date 2021/11/26 15:27
	 * @param inputStream
	 * @return com.relaxed.common.oss.s3.domain.StreamMeta
	 */
	public static StreamMeta convertToByteStreamMeta(InputStream inputStream) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		long size = 0;
		byte[] buffer = new byte[1024];
		int len;

		while ((len = inputStream.read(buffer)) > -1) {
			size += len;
			out.write(buffer, 0, len);
		}

		return new StreamMeta(size, new ByteArrayInputStream(out.toByteArray()));
	}

}

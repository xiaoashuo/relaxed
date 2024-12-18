package com.relaxed.common.oss.s3.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.utils.IoUtils;

import java.io.*;

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
	 * @param inputStream Caller is responsible for closing the given input stream.
	 * @return com.relaxed.common.oss.s3.domain.StreamMeta
	 */
	public static StreamMeta convertToByteStreamMeta(InputStream inputStream) throws IOException {
		byte[] byteContent = IoUtils.toByteArray(inputStream);
		long size = byteContent.length;
		return new StreamMeta(size, new ByteArrayInputStream(byteContent));
	}

}

package com.relaxed.common.oss.s3.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.utils.IoUtils;

import java.io.*;

/**
 * 流元数据类。 用于封装输入流及其大小信息，支持： 1. 流大小统计 2. 流内容读取 3. 字节流转换
 *
 * @author Yakir
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public class StreamMeta {

	/**
	 * 流大小，单位：字节
	 */
	private final Long size;

	/**
	 * 输入流
	 */
	private final InputStream inputStream;

	/**
	 * 将输入流转换为字节流元数据。 注意：调用者负责关闭输入流。
	 * @param inputStream 输入流
	 * @return 字节流元数据
	 * @throws IOException 当读取流内容时发生 I/O 错误
	 */
	public static StreamMeta convertToByteStreamMeta(InputStream inputStream) throws IOException {
		byte[] byteContent = IoUtils.toByteArray(inputStream);
		long size = byteContent.length;
		return new StreamMeta(size, new ByteArrayInputStream(byteContent));
	}

}

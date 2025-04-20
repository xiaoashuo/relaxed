package com.relaxed.common.core.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * HTTP请求体重复读取包装器。 该类继承自 {@link HttpServletRequestWrapper}，通过缓存请求体内容到内存中，
 * 使得请求体可以被多次读取。这在需要在多个地方读取请求体内容的场景下特别有用， 比如日志记录、请求验证、数据处理等多个环节都需要访问请求体数据。
 *
 * <p>
 * 主要特性：
 * <ul>
 * <li>支持请求体内容的重复读取</li>
 * <li>保持请求参数映射的一致性</li>
 * <li>处理流读取异常并提供日志记录</li>
 * </ul>
 *
 * <p>
 * 使用示例： <pre>{@code
 * HttpServletRequest wrappedRequest = new RepeatBodyRequestWrapper(originalRequest);
 * // 第一次读取请求体
 * String body1 = StreamUtils.copyToString(wrappedRequest.getInputStream(), StandardCharsets.UTF_8);
 * // 第二次读取请求体（原始请求会抛出异常）
 * String body2 = StreamUtils.copyToString(wrappedRequest.getInputStream(), StandardCharsets.UTF_8);
 * }</pre>
 *
 * @author Hccake
 * @since 1.0.0
 */
@Slf4j
public class RepeatBodyRequestWrapper extends HttpServletRequestWrapper {

	/**
	 * 请求体字节数组 用于存储原始请求体的内容，支持重复读取
	 */
	private final byte[] bodyByteArray;

	/**
	 * 请求参数映射 缓存原始请求的参数映射，避免重复解析
	 */
	private final Map<String, String[]> parameterMap;

	/**
	 * 创建一个新的请求包装器
	 * @param request 原始的HTTP请求对象
	 */
	public RepeatBodyRequestWrapper(HttpServletRequest request) {
		super(request);
		this.parameterMap = super.getParameterMap();
		this.bodyByteArray = getByteBody(request);
	}

	/**
	 * 获取请求体的字符流读取器 如果请求体为空，则返回null
	 * @return 字符流读取器，用于读取请求体内容
	 */
	@Override
	public BufferedReader getReader() {
		return ObjectUtils.isEmpty(bodyByteArray) ? null : new BufferedReader(new InputStreamReader(getInputStream()));
	}

	/**
	 * 获取请求体的输入流 每次调用都会返回一个新的基于缓存的字节数组的输入流， 这使得输入流可以被多次读取
	 * @return ServletInputStream 实例，允许重复读取请求体内容
	 */
	@Override
	public ServletInputStream getInputStream() {
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bodyByteArray);
		return new ServletInputStream() {
			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
				// 空实现，因为基于字节数组的流不需要异步读取支持
			}

			@Override
			public int read() {
				return byteArrayInputStream.read();
			}
		};
	}

	/**
	 * 从请求中读取请求体内容并转换为字节数组
	 * @param request HTTP请求对象
	 * @return 请求体的字节数组，如果读取失败则返回空数组
	 */
	private static byte[] getByteBody(HttpServletRequest request) {
		byte[] body = new byte[0];
		try {
			body = StreamUtils.copyToByteArray(request.getInputStream());
		}
		catch (IOException e) {
			log.error("解析流中数据异常", e);
		}
		return body;
	}

	/**
	 * 获取请求体的字节数组 直接返回内部存储的字节数组，方便外部直接访问请求体内容
	 * @return 请求体的字节数组
	 */
	public byte[] getBodyByteArray() {
		return this.bodyByteArray;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return parameterMap;
	}

}

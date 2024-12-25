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
 * @author Hccake
 * @version 1.0
 * @date 2019/10/16 10:14 Request包装类 1. body重复读取
 */
@Slf4j
public class RepeatBodyRequestWrapper extends HttpServletRequestWrapper {

	private final byte[] bodyByteArray;

	private final Map<String, String[]> parameterMap;

	public RepeatBodyRequestWrapper(HttpServletRequest request) {
		super(request);
		this.parameterMap = super.getParameterMap();
		this.bodyByteArray = getByteBody(request);
	}

	@Override
	public BufferedReader getReader() {
		return ObjectUtils.isEmpty(bodyByteArray) ? null : new BufferedReader(new InputStreamReader(getInputStream()));
	}

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
			}

			@Override
			public int read() {
				return byteArrayInputStream.read();
			}
		};
	}

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

	public byte[] getBodyByteArray() {
		return this.bodyByteArray;
	}

	/**
	 * 重写 getParameterMap() 方法 解决 undertow 中流被读取后，会进行标记，从而导致无法正确获取 body 中的表单数据的问题
	 * @see io.undertow.servlet.spec.HttpServletRequestImpl#readStarted
	 * @return Map<String, String[]> parameterMap
	 */
	@Override
	public Map<String, String[]> getParameterMap() {
		return parameterMap;
	}

}

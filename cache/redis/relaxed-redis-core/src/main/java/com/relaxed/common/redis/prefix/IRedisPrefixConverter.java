package com.relaxed.common.redis.prefix;

import cn.hutool.core.text.CharSequenceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * Redis key前缀转换器接口。 提供Redis key前缀的生成、匹配、添加和移除功能。
 *
 * @author huyuanzhi
 * @since 1.0
 */
public interface IRedisPrefixConverter {

	Logger LOGGER = LoggerFactory.getLogger(IRedisPrefixConverter.class);

	/**
	 * 获取Redis key的前缀
	 * @return Redis key的前缀
	 */
	String getPrefix();

	/**
	 * 判断是否需要进行前缀转换
	 * @return 如果需要添加前缀返回true，否则返回false
	 */
	boolean match();

	/**
	 * 移除key的前缀
	 * @param bytes 带有前缀的key字节数组
	 * @return 移除前缀后的原始key字节数组
	 */
	default byte[] unwrap(byte[] bytes) {
		int wrapLen;
		if (!match() || bytes == null || (wrapLen = bytes.length) == 0) {
			return bytes;
		}
		String prefix = getPrefix();
		if (CharSequenceUtil.isBlank(prefix)) {
			LOGGER.warn("prefix converter is enabled,but method getPrefix returns blank result,check your implement!");
			return bytes;
		}
		byte[] prefixBytes = prefix.getBytes(StandardCharsets.UTF_8);
		int prefixLen = prefixBytes.length;
		int originLen = wrapLen - prefixLen;
		byte[] originBytes = new byte[originLen];
		System.arraycopy(bytes, prefixLen, originBytes, 0, originLen);
		return originBytes;
	}

	/**
	 * 给key添加前缀
	 * @param bytes 原始key字节数组
	 * @return 添加前缀后的key字节数组
	 */
	default byte[] wrap(byte[] bytes) {
		int originLen;
		if (!match() || bytes == null || (originLen = bytes.length) == 0) {
			return bytes;
		}
		String prefix = getPrefix();
		if (CharSequenceUtil.isBlank(prefix)) {
			LOGGER.warn("prefix converter is enabled,but method getPrefix returns blank result,check your implement!");
			return bytes;
		}
		byte[] prefixBytes = prefix.getBytes(StandardCharsets.UTF_8);
		int prefixLen = prefixBytes.length;
		byte[] wrapBytes = new byte[prefixLen + originLen];
		System.arraycopy(prefixBytes, 0, wrapBytes, 0, prefixLen);
		System.arraycopy(bytes, 0, wrapBytes, prefixLen, originLen);
		return wrapBytes;
	}

}

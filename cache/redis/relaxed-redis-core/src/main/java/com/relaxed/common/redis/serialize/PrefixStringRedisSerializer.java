package com.relaxed.common.redis.serialize;

import com.relaxed.common.redis.prefix.IRedisPrefixConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.StandardCharsets;

/**
 * 带前缀的字符串序列化器。 继承自StringRedisSerializer，添加了全局key前缀功能。
 *
 * @author Hccake
 * @since 1.0
 */
@Slf4j
public class PrefixStringRedisSerializer extends StringRedisSerializer {

	/**
	 * Redis key前缀转换器
	 */
	private final IRedisPrefixConverter iRedisPrefixConverter;

	/**
	 * 构造函数
	 * @param iRedisPrefixConverter Redis key前缀转换器
	 */
	public PrefixStringRedisSerializer(IRedisPrefixConverter iRedisPrefixConverter) {
		super(StandardCharsets.UTF_8);
		this.iRedisPrefixConverter = iRedisPrefixConverter;
	}

	/**
	 * 反序列化方法，先移除前缀再反序列化
	 * @param bytes 带有前缀的序列化数据
	 * @return 反序列化后的字符串
	 */
	@Override
	public String deserialize(byte[] bytes) {
		byte[] unwrap = iRedisPrefixConverter.unwrap(bytes);
		return super.deserialize(unwrap);
	}

	/**
	 * 序列化方法，先序列化再添加前缀
	 * @param key 待序列化的字符串
	 * @return 添加前缀后的序列化数据
	 */
	@Override
	public byte[] serialize(String key) {
		byte[] originBytes = super.serialize(key);
		return iRedisPrefixConverter.wrap(originBytes);
	}

}

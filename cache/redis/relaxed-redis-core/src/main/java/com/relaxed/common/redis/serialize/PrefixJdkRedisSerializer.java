package com.relaxed.common.redis.serialize;

import com.relaxed.common.redis.prefix.IRedisPrefixConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

/**
 * 带前缀的JDK序列化器。 继承自JdkSerializationRedisSerializer，添加了全局key前缀功能。
 *
 * @author Hccake
 * @since 1.0
 */
@Slf4j
public class PrefixJdkRedisSerializer extends JdkSerializationRedisSerializer {

	/**
	 * Redis key前缀转换器
	 */
	private final IRedisPrefixConverter redisPrefixConverter;

	/**
	 * 构造函数
	 * @param redisPrefixConverter Redis key前缀转换器
	 */
	public PrefixJdkRedisSerializer(IRedisPrefixConverter redisPrefixConverter) {
		this.redisPrefixConverter = redisPrefixConverter;
	}

	/**
	 * 反序列化方法，先移除前缀再反序列化
	 * @param bytes 带有前缀的序列化数据
	 * @return 反序列化后的对象
	 */
	@Override
	public Object deserialize(byte[] bytes) {
		byte[] unwrap = redisPrefixConverter.unwrap(bytes);
		return super.deserialize(unwrap);
	}

	/**
	 * 序列化方法，先序列化再添加前缀
	 * @param object 待序列化的对象
	 * @return 添加前缀后的序列化数据
	 */
	@Override
	public byte[] serialize(Object object) {
		byte[] originBytes = super.serialize(object);
		return redisPrefixConverter.wrap(originBytes);
	}

}

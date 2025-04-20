package com.relaxed.common.redis.serialize;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 基于Jackson的Redis序列化器实现类。 使用Jackson ObjectMapper进行对象的序列化和反序列化。
 *
 * @author Yakir
 * @since 1.0
 */
@RequiredArgsConstructor
public class JacksonSerializerRelaxed implements RelaxedRedisSerializer {

	/**
	 * Jackson对象映射器
	 */
	private final ObjectMapper objectMapper;

	/**
	 * 将对象序列化为JSON字符串
	 * @param cacheData 待缓存的数据
	 * @return 序列化后的JSON字符串
	 * @throws IOException 序列化过程中可能发生的IO异常
	 */
	@Override
	public String serialize(Object cacheData) throws IOException {
		return objectMapper.writeValueAsString(cacheData);
	}

	/**
	 * 将JSON字符串反序列化为指定类型的对象
	 * @param cacheData 缓存中的JSON字符串
	 * @param type 反序列化目标类型
	 * @return 反序列化后的对象
	 * @throws IOException 反序列化过程中可能发生的IO异常
	 */
	@Override
	public Object deserialize(String cacheData, Type type) throws IOException {
		return objectMapper.readValue(cacheData, RelaxedRedisSerializer.getJavaType(type));
	}

}

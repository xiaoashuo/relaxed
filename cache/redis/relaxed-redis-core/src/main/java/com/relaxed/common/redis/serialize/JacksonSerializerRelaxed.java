package com.relaxed.common.redis.serialize;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author Yakir
 * @Topic JacksonSerializer
 * @Description
 * @date 2021/7/23 18:25
 * @Version 1.0
 */
@RequiredArgsConstructor
public class JacksonSerializerRelaxed implements RelaxedRedisSerializer {

	private final ObjectMapper objectMapper;

	/**
	 * 序列化方法
	 * @param cacheData 待缓存的数据
	 * @return 序列化后的数据
	 * @throws IOException IO异常
	 */
	@Override
	public String serialize(Object cacheData) throws IOException {
		return objectMapper.writeValueAsString(cacheData);
	}

	/**
	 * 反序列化方法
	 * @param cacheData 缓存中的数据
	 * @param type 反序列化目标类型
	 * @return 反序列化后的对象
	 * @throws IOException IO异常
	 */
	@Override
	public Object deserialize(String cacheData, Type type) throws IOException {
		return objectMapper.readValue(cacheData, RelaxedRedisSerializer.getJavaType(type));
	}

}

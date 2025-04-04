package com.relaxed.common.redis.serialize;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Yakir
 * @Topic CacheSerialize
 * @Description 缓存序列化
 * @date 2021/7/23 18:24
 * @Version 1.0
 */
public interface RelaxedRedisSerializer {

	/**
	 * 序列化方法
	 * @param cacheData
	 * @return
	 * @throws IOException
	 */
	String serialize(Object cacheData) throws IOException;

	/**
	 * 反序列化方法
	 * @param cacheData
	 * @param type
	 * @return
	 * @throws IOException
	 */
	Object deserialize(String cacheData, Type type) throws IOException;

	/**
	 * Type转JavaType
	 * @param type
	 * @return
	 */
	static JavaType getJavaType(Type type) {
		// 判断是否带有泛型
		if (type instanceof ParameterizedType) {
			Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
			// 获取泛型类型
			Class rowClass = (Class) ((ParameterizedType) type).getRawType();

			JavaType[] javaTypes = new JavaType[actualTypeArguments.length];

			for (int i = 0; i < actualTypeArguments.length; i++) {
				// 泛型也可能带有泛型，递归获取
				javaTypes[i] = getJavaType(actualTypeArguments[i]);
			}
			return TypeFactory.defaultInstance().constructParametricType(rowClass, javaTypes);
		}
		else {
			// 简单类型直接用该类构建JavaType
			Class cla = (Class) type;
			return TypeFactory.defaultInstance().constructParametricType(cla, new JavaType[0]);
		}
	}

}

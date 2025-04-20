package com.relaxed.common.redis.serialize;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Redis序列化器接口，定义了对对象进行序列化和反序列化的方法。 支持将对象序列化为字符串，以及将字符串反序列化为指定类型的对象。
 * 提供了Type到JavaType的转换工具方法。
 *
 * @author Yakir
 * @since 1.0
 */
public interface RelaxedRedisSerializer {

	/**
	 * 将对象序列化为字符串
	 * @param cacheData 待序列化的对象
	 * @return 序列化后的字符串
	 * @throws IOException 序列化过程中可能发生的IO异常
	 */
	String serialize(Object cacheData) throws IOException;

	/**
	 * 将字符串反序列化为指定类型的对象
	 * @param cacheData 待反序列化的字符串
	 * @param type 目标类型
	 * @return 反序列化后的对象
	 * @throws IOException 反序列化过程中可能发生的IO异常
	 */
	Object deserialize(String cacheData, Type type) throws IOException;

	/**
	 * 将Type转换为Jackson的JavaType
	 * @param type 待转换的Type
	 * @return 转换后的JavaType
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

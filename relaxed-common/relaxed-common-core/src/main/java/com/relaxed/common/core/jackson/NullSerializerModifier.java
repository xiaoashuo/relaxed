package com.relaxed.common.core.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.util.Annotations;

import com.relaxed.common.core.jackson.annotations.IgnoreNullSerializer;
import com.relaxed.common.core.jackson.annotations.IgnoreNullSerializerByType;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/17 23:19
 */
public class NullSerializerModifier extends BeanSerializerModifier {

	private final JsonSerializer<Object> nullArrayJsonSerializer = new NullArrayJsonSerializer();

	private final JsonSerializer<Object> nullMapJsonSerializer = new NullMapJsonSerializer();

	private final JsonSerializer<Object> nullStringJsonSerializer = new NullStringJsonSerializer();

	@Override
	public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
			List<BeanPropertyWriter> beanProperties) {
		Annotations classAnnotations = beanDesc.getClassAnnotations();

		IgnoreNullSerializerByType ignoreNullSerializer = classAnnotations.get(IgnoreNullSerializerByType.class);
		/// 若用户未指定注解 则所有都赋予默认空序列化器
		if (ignoreNullSerializer == null) {
			// 循环所有的beanPropertyWriter
			for (BeanPropertyWriter writer : beanProperties) {
				boolean isIgnoreNullSerializer = writer.getAnnotation(IgnoreNullSerializer.class) != null;
				if (isIgnoreNullSerializer) {
					// 若存在忽略空值序列化器,直接跳过
					continue;
				}
				if (isStringType(writer)) {
					// null 字符串转空
					writer.assignNullSerializer(this.nullStringJsonSerializer);
				}
				else if (isArrayType(writer)) {
					// null array 或 list，set则注册nullSerializer
					writer.assignNullSerializer(this.nullArrayJsonSerializer);
				}
				else if (isMapType(writer)) {
					// null Map 转 '{}'
					writer.assignNullSerializer(this.nullMapJsonSerializer);
				}
			}
			return beanProperties;
		}
		IgnoreNullSerializerByType.Include value = ignoreNullSerializer.value();
		// 忽略所有 则所有都不赋予空序列化器
		if (IgnoreNullSerializerByType.Include.ALL.equals(value)) {
			return beanProperties;
		}
		// 循环所有的beanPropertyWriter
		for (BeanPropertyWriter writer : beanProperties) {
			// 若存在忽略空值序列化器,直接跳过空序列化器分配,否则走全局配置
			boolean isIgnoreNullSerializer = writer.getAnnotation(IgnoreNullSerializer.class) != null;
			if (isIgnoreNullSerializer) {
				continue;
			}
			if (!IgnoreNullSerializerByType.Include.STRING.equals(value) && isStringType(writer)) {
				// null 字符串转空
				writer.assignNullSerializer(this.nullStringJsonSerializer);
			}
			else if (!IgnoreNullSerializerByType.Include.ARRAY.equals(value) && isArrayType(writer)) {
				// null array 或 list，set则注册nullSerializer
				writer.assignNullSerializer(this.nullArrayJsonSerializer);
			}
			else if (!IgnoreNullSerializerByType.Include.MAP.equals(value) && isMapType(writer)) {
				// null Map 转 '{}'
				writer.assignNullSerializer(this.nullMapJsonSerializer);
			}
		}
		return beanProperties;
	}

	/**
	 * 是否是 String 类型
	 * @param writer BeanPropertyWriter
	 * @return boolean
	 */
	private boolean isStringType(BeanPropertyWriter writer) {
		Class<?> clazz = writer.getType().getRawClass();
		return String.class.isAssignableFrom(clazz);
	}

	/**
	 * 是否是Map类型
	 * @param writer BeanPropertyWriter
	 * @return boolean
	 */
	private boolean isMapType(BeanPropertyWriter writer) {
		Class<?> clazz = writer.getType().getRawClass();
		return Map.class.isAssignableFrom(clazz);
	}

	/**
	 * 是否是集合类型
	 * @param writer BeanPropertyWriter
	 * @return boolean
	 */
	private boolean isArrayType(BeanPropertyWriter writer) {
		Class<?> clazz = writer.getType().getRawClass();
		return clazz.isArray() || Collection.class.isAssignableFrom(clazz);

	}

}

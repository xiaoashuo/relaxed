package com.relaxed.common.log.biz.util;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 扁平化映射工具类 该工具类提供了对象扁平化处理的相关功能 主要功能包括： 1. 将嵌套对象转换为扁平化的键值对 2. 支持自定义分隔符和前缀 3. 处理复杂对象的序列化 4.
 * 异常处理和日志记录
 *
 * @author Yakir
 */
@Slf4j
public class FlatMapUtil {

	/**
	 * Jackson 对象映射器 配置了日期时间模块和格式化输出
	 */
	private static final ObjectMapper objectMapper = new ObjectMapper();

	static {
		// 注册 Java 8 日期时间模块
		objectMapper.registerModule(new JavaTimeModule());
		// 配置格式化输出
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
	}

	/**
	 * 将对象转换为扁平化的 Map 使用默认分隔符（.）和空前缀
	 * @param obj 要转换的对象
	 * @return 扁平化的 Map
	 */
	public static Map<String, Object> toMap(Object obj) {
		return toMap(obj, null, null);
	}

	/**
	 * 将对象转换为扁平化的 Map 使用指定的分隔符和前缀
	 * @param obj 要转换的对象
	 * @param separator 键分隔符，默认为点号（.）
	 * @param prefix 键前缀，默认为空
	 * @return 扁平化的 Map
	 */
	public static Map<String, Object> toMap(Object obj, String separator, String prefix) {
		Map<String, Object> map = new HashMap<>();
		if (obj == null) {
			return map;
		}
		if (StrUtil.isEmpty(separator)) {
			separator = ".";
		}
		try {
			// 将对象转换为 JSON 字符串
			String json = objectMapper.writeValueAsString(obj);
			// 将 JSON 字符串转换为 Map
			Map<String, Object> jsonMap = objectMapper.readValue(json, Map.class);
			// 扁平化处理
			flattenMap(jsonMap, map, separator, prefix);
		}
		catch (JsonProcessingException e) {
			log.error("json序列化失败", e);
		}
		return map;
	}

	/**
	 * 递归扁平化 Map 将嵌套的 Map 结构转换为扁平化的键值对
	 * @param source 源 Map
	 * @param target 目标 Map
	 * @param separator 键分隔符
	 * @param prefix 键前缀
	 */
	private static void flattenMap(Map<String, Object> source, Map<String, Object> target, String separator,
			String prefix) {
		source.forEach((key, value) -> {
			String newKey = StrUtil.isEmpty(prefix) ? key : prefix + separator + key;
			if (value instanceof Map) {
				// 递归处理嵌套的 Map
				flattenMap((Map<String, Object>) value, target, separator, newKey);
			}
			else {
				// 直接添加非 Map 类型的值
				target.put(newKey, value);
			}
		});
	}

}

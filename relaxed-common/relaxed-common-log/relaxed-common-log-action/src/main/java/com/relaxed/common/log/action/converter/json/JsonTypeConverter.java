package com.relaxed.common.log.action.converter.json;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.relaxed.common.log.action.annotation.LogTag;
import com.relaxed.common.log.action.converter.DiffConverter;
import com.relaxed.common.log.action.utils.FlatMapUtil;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Yakir
 * @Topic JsonTypeConverter
 * @Description
 * @date 2021/12/15 10:31
 * @Version 1.0
 */
public class JsonTypeConverter implements DiffConverter {

	@Override
	public String diffValue(Field field, LogTag logTag, Object oldFieldValue, Object newFieldValue) {
		return null;
	}

	public static void main(String[] args) throws JsonProcessingException {
		String original = "{\"version\":\"1.0.0\",\"content\":[{\"lineNumber\":1,\"gender\":\"男\"}]}";
		String targetJson = "{\"version\":\"1.0.0\",\"content\":[{\"lineNumber\":2,\"age\":\"18\"}]}";

		// MapDifference difference = Maps.difference(sourceMap, targetMap);
		// List<JsonModel> jsonModels=new ArrayList<>();
		// Map<String, String> result = new HashMap<>();
		// mapProc(sourceMap, targetMap, result, "");
		// ObjectMapper mapper = new ObjectMapper();
		//
		// TypeReference<HashMap<String, Object>> type =
		// new TypeReference<HashMap<String, Object>>() {};
		//
		//
		// HashMap<String, Object> j1 = mapper.readValue(original,type);
		// HashMap<String, Object> j2 = mapper.readValue(targetJson,type);
		Map<String, Object> sourceMap = JSONUtil.toBean(original, Map.class);
		Map<String, Object> targetMap = JSONUtil.toBean(targetJson, Map.class);
		Map<String, Object> flatten1 = FlatMapUtil.flatten(sourceMap);
		Map<String, Object> flatten2 = FlatMapUtil.flatten(targetMap);
		MapDifference<String, Object> difference = Maps.difference(flatten1, flatten2);
		// 是否有差异，返回boolean
		boolean areEqual = difference.areEqual();

		// 建只存在于左边映射项 即为删除操作
		Map<String, Object> onlyLeftMap = difference.entriesOnlyOnLeft();
		// 建只存在于右边
		Map<String, Object> onlyRightMap = difference.entriesOnlyOnRight();
		// 两个map的交集
		Map<String, Object> entriesInCommon = difference.entriesInCommon();

		// 键相同但是值不同值映射项。返回的Map的值类型为MapDifference.ValueDifference，以表示左右两个不同的值
		Map<String, MapDifference.ValueDifference<Object>> entriesDiffering = difference.entriesDiffering();

		System.out.println("结束");
	}

	interface ResultHandle {

		void handle(String key, String sourceValue, String targetValue);

	}

	@Data
	class JsonModel {

		private String jsonName;

		private String sourceJsonValue;

		private String targetJsonValue;

		private String diffValue;

		private String action;

	}

}

package com.relaxed.common.log.action.converter.json;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.relaxed.common.log.action.annotation.LogTag;
import com.relaxed.common.log.action.converter.DiffConverter;
import com.relaxed.common.log.action.utils.FlatMapUtil;
import com.relaxed.common.log.action.utils.JsonUtil;
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
		return JsonUtil.jsonDiff(StrUtil.toString(oldFieldValue), StrUtil.toString(newFieldValue));
	}

	public static void main(String[] args) throws JsonProcessingException {
		String original = "{\"version\":\"1.0.0\",\"content\":[{\"lineNumber\":1,\"gender\":\"男\"}]}";
		String targetJson = "{\"version\":\"1.0.0\",\"content\":[{\"lineNumber\":2,\"age\":\"18\"}]}";

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

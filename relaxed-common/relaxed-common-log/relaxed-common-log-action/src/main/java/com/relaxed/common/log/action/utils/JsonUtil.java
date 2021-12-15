package com.relaxed.common.log.action.utils;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.DiffFlags;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.diff.JsonDiff;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import foodev.jsondiff.Jackson2Diff;
import org.springframework.util.Assert;

import java.util.EnumSet;
import java.util.Map;

/**
 * @author Yakir
 * @Topic JsonUtil
 * @Description JSON 差异方案 https://blog.csdn.net/liuxiao723846/article/details/108508200 1.
 * JSON patch json-patch 不支持关闭move、copy operations
 * https://blog.csdn.net/liuxiao723846/article/details/108547980
 * https://github.com/java-json-tools/json-patch/releases 2. zjsonpatch 支持关闭move、copy
 * operations https://github.com/flipkart-incubator/zjsonpatch 3.铺平json 然后用google的guava
 * 的map Diffenrents https://blog.csdn.net/Revivedsun/article/details/118463049 4.json diff
 * https://github.com/algesten/jsondiff
 * @date 2021/12/15 13:41
 * @Version 1.0
 */
public class JsonUtil {

	public static void main(String[] args) throws JsonProcessingException {
		String expected = "{\"username\":\"张三\",\"version\":\"1.0.0\",\"content\":[{\"lineNumber\":1,\"gender\":\"女\"}]}";
		String actual = "{\"version\":\"1.0.0\",\"content\":[{\"lineNumber\":1,\"age\":\"18\"}]}";
		// 方案1json-patch铺平json
		ObjectMapper mapper = new ObjectMapper();
		JsonPatch patch = JsonDiff.asJsonPatch(mapper.readTree(expected), mapper.readTree(actual));
		String result = "[op: remove; path: \"/username\", op: remove; path: \"/content/0/gender\", op: add; path: \"/content/0/age\"; value: \"18\"]";
		Assert.state(result.equals(patch.toString()), "结果不一致");
		System.out.println(patch);
		// 方案2 铺平json
		Map<String, Object> sourceMap = JSONUtil.toBean(expected, Map.class);
		Map<String, Object> targetMap = JSONUtil.toBean(actual, Map.class);
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

		// 方案3 zjsonpatch
		EnumSet<DiffFlags> flags = DiffFlags.dontNormalizeOpIntoMoveAndCopy().clone();
		JsonNode jsonpPatch = JsonDiff.asJson(mapper.readTree(expected), mapper.readTree(actual));
		System.out.println(jsonpPatch.toPrettyString());
		// 方案4 json diff
		foodev.jsondiff.JsonDiff diff = new Jackson2Diff();
		String diff1 = diff.diff(expected, actual);
		System.out.println(diff1);
	}

}

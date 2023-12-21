package com.relaxed.common.log.biz.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.DiffFlags;
import com.flipkart.zjsonpatch.JsonDiff;
import lombok.SneakyThrows;

import java.util.EnumSet;

/**
 * @author Yakir
 * @Topic JsonUtil
 * @Description JSON 差异方案
 * <p>
 * https://blog.csdn.net/liuxiao723846/article/details/108508200 1.JSON patch json-patch
 * 不支持关闭move、copy operations<br/>
 * https://blog.csdn.net/liuxiao723846/article/details/108547980 <br/>
 * https://github.com/java-json-tools/json-patch/releases
 *
 * 2. zjsonpatch 支持关闭move、copy operations <br/>
 * https://github.com/flipkart-incubator/zjsonpatch <br/>
 * 3.铺平json 然后用google的guava 的map Diffenrents <br/>
 * https://blog.csdn.net/Revivedsun/article/details/118463049 <br/>
 * 4.json diff <br/>
 * https://github.com/algesten/jsondiff
 * </p>
 * @date 2021/12/15 13:41
 * @Version 1.0
 */
public class JsonUtil {

	private static ObjectMapper MAPPER = new ObjectMapper();

	private static EnumSet<DiffFlags> flags = EnumSet
			.of(DiffFlags.OMIT_MOVE_OPERATION, DiffFlags.OMIT_COPY_OPERATION, DiffFlags.ADD_ORIGINAL_VALUE_ON_REPLACE)
			.clone();

	/**
	 * 提取 json差异
	 * @author yakir
	 * @date 2021/12/15 15:15
	 * @param expected 支持 json对象 json数组
	 * @param actual 支持 json对象 json数组
	 * @return java.lang.String 返回差异json数组
	 */
	public static String jsonDiff(String expected, String actual) {
		return jsonPatchDiff(expected, actual).toPrettyString();
	}

	@SneakyThrows
	public static JsonNode jsonPatchDiff(String expected, String actual) {
		JsonNode source = MAPPER.readTree(expected);
		JsonNode target = MAPPER.readTree(actual);
		JsonNode jsonpPatch = jsonDiff(source, target);
		return jsonpPatch;
	}

	public static JsonNode jsonDiff(JsonNode expected, JsonNode actual) {
		return jsonDiff(expected, actual, flags);
	}

	public static JsonNode jsonDiff(JsonNode expected, JsonNode actual, EnumSet<DiffFlags> enumSet) {
		JsonNode jsonpPatch = JsonDiff.asJson(expected, actual, enumSet);
		return jsonpPatch;
	}

	public static void main(String[] args) throws JsonProcessingException {
		String expected = "{\"username\":\"张三\",\"version\":\"1.0.0\",\"content\":[{\"lineNumber\":1,\"gender\":\"女\"}]}";
		String actual = "{\"jack\":\"张三\",\"version\":\"1.0.0\",\"content\":[{\"lineNumber\":2,\"age\":\"18\"}]}";
		// expected="[{\n" +
		// "\t\"username\": \"lisa\"\n" +
		// "}, {\n" +
		// "\t\"username\": \"sa\"\n" +
		// "}]";
		// actual="[{\n" +
		// "\t\"username\": \"df\"\n" +
		// "}, {\n" +
		// "\t\"username\": \"af\"\n" +
		// "}]";
		String jsonDiff = jsonDiff(expected, actual);
		System.out.println(jsonDiff);
		// // 方案1json-patch铺平json
		// ObjectMapper mapper = new ObjectMapper();
		// JsonPatch patch = JsonDiff.asJsonPatch(mapper.readTree(expected),
		// mapper.readTree(actual));
		// String result = "[op: remove; path: \"/username\", op: remove; path:
		// \"/content/0/gender\", op: add; path: \"/content/0/age\"; value: \"18\"]";
		//
		// System.out.println(patch);
		// // 方案2 铺平json
		// Map<String, Object> sourceMap = JSONUtil.toBean(expected, Map.class);
		// Map<String, Object> targetMap = JSONUtil.toBean(actual, Map.class);
		// Map<String, Object> flatten1 = FlatMapUtil.flatten(sourceMap);
		// Map<String, Object> flatten2 = FlatMapUtil.flatten(targetMap);
		// MapDifference<String, Object> difference = Maps.difference(flatten1, flatten2);
		// // 是否有差异，返回boolean
		// boolean areEqual = difference.areEqual();
		// // 建只存在于左边映射项 即为删除操作
		// Map<String, Object> onlyLeftMap = difference.entriesOnlyOnLeft();
		// // 建只存在于右边
		// Map<String, Object> onlyRightMap = difference.entriesOnlyOnRight();
		// // 两个map的交集
		// Map<String, Object> entriesInCommon = difference.entriesInCommon();
		// // 键相同但是值不同值映射项。返回的Map的值类型为MapDifference.ValueDifference，以表示左右两个不同的值
		// Map<String, MapDifference.ValueDifference<Object>> entriesDiffering =
		// difference.entriesDiffering();

		// 方案3 zjsonpatch
		// EnumSet<DiffFlags> flags = EnumSet.of(DiffFlags.OMIT_MOVE_OPERATION,
		// DiffFlags.OMIT_COPY_OPERATION,DiffFlags.ADD_ORIGINAL_VALUE_ON_REPLACE).clone();
		// JsonNode source = mapper.readTree(expected);
		// JsonNode jsonpPatch = JsonDiff.asJson(source, mapper.readTree(actual),flags);
		// System.out.println(jsonpPatch.toPrettyString());
		// JsonNode target = JsonPatch.apply(jsonpPatch, source);
		// System.out.println(target);
		// // 方案4 json diff
		// foodev.jsondiff.JsonDiff diff = new Jackson2Diff();
		// String diff1 = diff.diff(expected, actual);
		// System.out.println(diff1);
	}

}

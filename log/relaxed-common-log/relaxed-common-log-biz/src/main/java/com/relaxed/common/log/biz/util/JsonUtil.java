package com.relaxed.common.log.biz.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.DiffFlags;
import com.flipkart.zjsonpatch.JsonDiff;
import com.relaxed.common.log.biz.enums.AttrOptionEnum;
import com.relaxed.common.log.biz.model.AttributeChange;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

/**
 * JSON 工具类 该工具类提供了 JSON 差异比较和转换的相关功能 主要功能包括： 1. 比较两个 JSON 字符串的差异 2. 将 JSON 差异转换为属性变更列表 3.
 * 支持多种差异比较策略 4. 处理 JSON 节点的值提取
 *
 * 参考实现： 1. JSON Patch (json-patch) - 不支持关闭 move、copy 操作 2. zjsonpatch - 支持关闭 move、copy 操作
 * 3. 扁平化 JSON 后使用 Guava 的 Map Differences 4. json-diff
 *
 * @author Yakir
 */
public class JsonUtil {

	/**
	 * 操作类型字段名
	 */
	public static final String OP = "op";

	/**
	 * 值字段名
	 */
	public static final String VALUE = "value";

	/**
	 * 路径字段名
	 */
	public static final String PATH = "path";

	/**
	 * 来源字段名
	 */
	public static final String FROM = "from";

	/**
	 * 来源值字段名
	 */
	public static final String FROM_VALUE = "fromValue";

	/**
	 * Jackson 对象映射器
	 */
	private static ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * 差异比较标志 配置为忽略移动和复制操作，并在替换操作中包含原始值
	 */
	private static EnumSet<DiffFlags> flags = EnumSet
			.of(DiffFlags.OMIT_MOVE_OPERATION, DiffFlags.OMIT_COPY_OPERATION, DiffFlags.ADD_ORIGINAL_VALUE_ON_REPLACE)
			.clone();

	/**
	 * 比较两个 JSON 字符串的差异 返回格式化的差异结果
	 * @param expected 原始 JSON 字符串
	 * @param actual 目标 JSON 字符串
	 * @return 格式化的差异结果
	 */
	public static String jsonDiff(String expected, String actual) {
		return jsonPatchDiff(expected, actual).toPrettyString();
	}

	/**
	 * 比较两个 JSON 字符串的差异 返回差异的 JsonNode 对象
	 * @param expected 原始 JSON 字符串
	 * @param actual 目标 JSON 字符串
	 * @return 差异的 JsonNode 对象
	 */
	@SneakyThrows
	public static JsonNode jsonPatchDiff(String expected, String actual) {
		JsonNode source = MAPPER.readTree(expected);
		JsonNode target = MAPPER.readTree(actual);
		return jsonDiff(source, target);
	}

	/**
	 * 将 JSON 差异转换为属性变更列表
	 * @param expected 原始 JSON 字符串
	 * @param actual 目标 JSON 字符串
	 * @return 属性变更列表
	 */
	public static List<AttributeChange> diffJson(String expected, String actual) {
		JsonNode jsonNode = jsonPatchDiff(expected, actual);
		Iterator<JsonNode> elements = jsonNode.elements();
		List<AttributeChange> attributeChangeList = new ArrayList<>();
		while (elements.hasNext()) {
			JsonNode node = elements.next();
			AttributeChange attributeChange = new AttributeChange();
			String op = nodeValue(node.get(OP), "unknown");
			String value = nodeValue(node.get(VALUE), "");
			String path = nodeValue(node.get(PATH), "");
			String fromValue = nodeValue(node.get(FROM_VALUE), "");
			AttrOptionEnum attrOptionEnum = AttrOptionEnum.of(op);
			attributeChange.setOp(attrOptionEnum.toString());
			attributeChange.setPath(path);
			String property = "";
			if (StringUtils.hasText(path)) {
				String[] pathArray = path.split("/");
				property = pathArray[pathArray.length - 1];
			}
			attributeChange.setProperty(property);
			if (AttrOptionEnum.ADD.equals(attrOptionEnum)) {
				attributeChange.setRightValue(value);
			}
			else if (AttrOptionEnum.REMOVE.equals(attrOptionEnum)) {
				attributeChange.setLeftValue(value);
			}
			else {
				attributeChange.setLeftValue(fromValue);
				attributeChange.setRightValue(value);
			}
			attributeChangeList.add(attributeChange);
		}
		return attributeChangeList;
	}

	/**
	 * 获取 JsonNode 的文本值 如果节点为 null，返回默认值
	 * @param jsonNode JSON 节点
	 * @param defaultValue 默认值
	 * @return 节点的文本值或默认值
	 */
	public static String nodeValue(JsonNode jsonNode, String defaultValue) {
		return jsonNode == null ? defaultValue : jsonNode.asText();
	}

	/**
	 * 比较两个 JsonNode 的差异 使用默认的比较标志
	 * @param expected 原始 JsonNode
	 * @param actual 目标 JsonNode
	 * @return 差异的 JsonNode
	 */
	public static JsonNode jsonDiff(JsonNode expected, JsonNode actual) {
		return jsonDiff(expected, actual, flags);
	}

	/**
	 * 比较两个 JsonNode 的差异 使用指定的比较标志
	 * @param expected 原始 JsonNode
	 * @param actual 目标 JsonNode
	 * @param enumSet 比较标志集合
	 * @return 差异的 JsonNode
	 */
	public static JsonNode jsonDiff(JsonNode expected, JsonNode actual, EnumSet<DiffFlags> enumSet) {
		return JsonDiff.asJson(expected, actual, enumSet);
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

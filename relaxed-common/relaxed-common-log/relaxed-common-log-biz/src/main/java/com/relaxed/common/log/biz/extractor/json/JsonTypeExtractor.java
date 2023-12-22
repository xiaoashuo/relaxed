package com.relaxed.common.log.biz.extractor.json;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;

import com.relaxed.common.log.biz.annotation.LogDiffTag;
import com.relaxed.common.log.biz.enums.AttrOptionEnum;
import com.relaxed.common.log.biz.extractor.DiffExtractor;
import com.relaxed.common.log.biz.model.AttributeChange;
import com.relaxed.common.log.biz.util.JsonUtil;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Yakir
 * @Topic JsonTypeConverter
 * @Description
 * @date 2021/12/15 10:31
 * @Version 1.0
 */
public class JsonTypeExtractor implements DiffExtractor {

	public static final String OP = "op";

	public static final String VALUE = "value";

	public static final String PATH = "path";

	public static final String FROM = "from";

	public static final String FROM_VALUE = "fromValue";

	@Override
	public String diffValue(Field field, LogDiffTag logDiffTag, Object oldFieldValue, Object newFieldValue) {
		JsonNode jsonNode = JsonUtil.jsonPatchDiff(StrUtil.toString(oldFieldValue), StrUtil.toString(newFieldValue));
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

		return JSONUtil.toJsonStr(attributeChangeList);
	}

	public String nodeValue(JsonNode jsonNode, String defaultValue) {
		return jsonNode == null ? defaultValue : jsonNode.asText();
	}

}

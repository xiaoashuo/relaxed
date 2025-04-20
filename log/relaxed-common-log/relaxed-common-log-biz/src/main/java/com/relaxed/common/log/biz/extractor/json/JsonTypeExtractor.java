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
 * JSON 类型差异提取器，用于处理 JSON 格式数据的差异比较。 该实现类通过比较 JSON 数据的旧值和新值，生成包含变更信息的属性变更列表。 主要用于处理 JSON
 * 格式的字段变更记录。
 *
 * @author Yakir
 * @since 1.0.0
 */
public class JsonTypeExtractor implements DiffExtractor {

	/**
	 * 提取并转换 JSON 字段的差异值
	 * @param field 字段对象
	 * @param logDiffTag 差异标签注解
	 * @param oldFieldValue 字段的旧值
	 * @param newFieldValue 字段的新值
	 * @return 属性变更列表的 JSON 字符串表示
	 */
	@Override
	public String diffValue(Field field, LogDiffTag logDiffTag, Object oldFieldValue, Object newFieldValue) {
		List<AttributeChange> attributeChangeList = JsonUtil.diffJson(StrUtil.toString(oldFieldValue),
				StrUtil.toString(newFieldValue));
		return JSONUtil.toJsonStr(attributeChangeList);
	}

	/**
	 * 获取 JSON 节点的值，如果节点为空则返回默认值
	 * @param jsonNode JSON 节点
	 * @param defaultValue 默认值
	 * @return JSON 节点的文本值或默认值
	 */
	public String nodeValue(JsonNode jsonNode, String defaultValue) {
		return jsonNode == null ? defaultValue : jsonNode.asText();
	}

}

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

	@Override
	public String diffValue(Field field, LogDiffTag logDiffTag, Object oldFieldValue, Object newFieldValue) {
		List<AttributeChange> attributeChangeList = JsonUtil.diffJson(StrUtil.toString(oldFieldValue),
				StrUtil.toString(newFieldValue));
		return JSONUtil.toJsonStr(attributeChangeList);
	}

	public String nodeValue(JsonNode jsonNode, String defaultValue) {
		return jsonNode == null ? defaultValue : jsonNode.asText();
	}

}

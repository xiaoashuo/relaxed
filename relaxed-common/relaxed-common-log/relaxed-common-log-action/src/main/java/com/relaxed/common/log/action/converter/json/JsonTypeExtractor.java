package com.relaxed.common.log.action.converter.json;

import cn.hutool.core.util.StrUtil;

import com.relaxed.common.log.action.annotation.LogTag;
import com.relaxed.common.log.action.converter.DiffExtractor;
import com.relaxed.common.log.action.utils.JsonUtil;

import java.lang.reflect.Field;

/**
 * @author Yakir
 * @Topic JsonTypeConverter
 * @Description
 * @date 2021/12/15 10:31
 * @Version 1.0
 */
public class JsonTypeExtractor implements DiffExtractor {

	@Override
	public String diffValue(Field field, LogTag logTag, Object oldFieldValue, Object newFieldValue) {
		return JsonUtil.jsonDiff(StrUtil.toString(oldFieldValue), StrUtil.toString(newFieldValue));
	}

}

package com.relaxed.common.log.action.converter;

import cn.hutool.core.util.StrUtil;
import com.relaxed.common.log.action.annotation.LogTag;

import java.lang.reflect.Field;

/**
 * @author Yakir
 * @Topic DefaultTypeConverter
 * @Description
 * @date 2021/12/14 17:28
 * @Version 1.0
 */
public class DefaultTypeDiffConverter implements DiffConverter {

	@Override
	public String diffValue(Field field, LogTag logTag, Object oldFieldValue, Object newFieldValue) {
		return "值:" + StrUtil.toString(oldFieldValue) + "->" + StrUtil.toString(newFieldValue);
	}

}

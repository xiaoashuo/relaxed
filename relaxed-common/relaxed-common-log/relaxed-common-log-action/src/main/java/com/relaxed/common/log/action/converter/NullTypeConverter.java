package com.relaxed.common.log.action.converter;

import com.relaxed.common.log.action.annotation.LogTag;

import java.lang.reflect.Field;

/**
 * @author Yakir
 * @Topic NullTypeConverter
 * @Description
 * @date 2021/12/14 17:46
 * @Version 1.0
 */
public class NullTypeConverter implements DiffConverter {

	@Override
	public String diffValue(Field field, LogTag logTag, Object oldFieldValue, Object newFieldValue) {
		return "";
	}

}

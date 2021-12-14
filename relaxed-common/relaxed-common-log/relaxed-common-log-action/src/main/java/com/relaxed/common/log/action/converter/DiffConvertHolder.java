package com.relaxed.common.log.action.converter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yakir
 * @Topic DiffConvertHolder
 * @Description
 * @date 2021/12/14 17:39
 * @Version 1.0
 */
public class DiffConvertHolder {

	private static Map<Class<? extends DiffConverter>, DiffConverter> CONVERTER_HOLDER = new HashMap<>();
	static {
		CONVERTER_HOLDER.put(NullTypeConverter.class, new NullTypeConverter());
		CONVERTER_HOLDER.put(DefaultTypeDiffConverter.class, new DefaultTypeDiffConverter());
	}

	public static DiffConverter getByClass(Class<? extends DiffConverter> clazz) {
		return CONVERTER_HOLDER.get(clazz);
	}

}

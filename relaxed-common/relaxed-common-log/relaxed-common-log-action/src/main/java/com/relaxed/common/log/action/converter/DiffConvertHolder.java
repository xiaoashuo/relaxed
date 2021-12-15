package com.relaxed.common.log.action.converter;

import com.relaxed.common.log.action.converter.json.JsonTypeConverter;
import com.relaxed.common.log.action.converter.richtext.RichTextTypeConverter;

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
		CONVERTER_HOLDER.put(SimpleTypeDiffConverter.class, new SimpleTypeDiffConverter());
		CONVERTER_HOLDER.put(RichTextTypeConverter.class, new RichTextTypeConverter());
		CONVERTER_HOLDER.put(JsonTypeConverter.class, new JsonTypeConverter());
	}

	public static DiffConverter getByClass(Class<? extends DiffConverter> clazz) {
		return CONVERTER_HOLDER.get(clazz);
	}

	/**
	 * 注册转换器
	 * @author yakir
	 * @date 2021/12/15 15:19
	 * @param clazz
	 * @param diffConverter
	 */
	public static void register(Class<? extends DiffConverter> clazz, DiffConverter diffConverter) {
		CONVERTER_HOLDER.put(clazz, diffConverter);
	}

	/**
	 * 默认使用简单类型处理器
	 * @author yakir
	 * @date 2021/12/15 10:06
	 * @return com.relaxed.common.log.action.converter.DiffConverter
	 */
	public static DiffConverter getByDefault() {
		return CONVERTER_HOLDER.get(SimpleTypeDiffConverter.class);
	}

}

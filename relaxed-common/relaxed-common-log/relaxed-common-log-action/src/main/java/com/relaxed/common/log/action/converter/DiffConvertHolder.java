package com.relaxed.common.log.action.converter;

import com.relaxed.common.log.action.converter.json.JsonTypeExtractor;
import com.relaxed.common.log.action.converter.richtext.RichTextTypeExtractor;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @author Yakir
 * @Topic DiffConvertHolder
 * @Description
 * @date 2021/12/14 17:39
 * @Version 1.0
 */
public class DiffConvertHolder {

	private static Map<Class<? extends DiffExtractor>, DiffExtractor> CONVERTER_HOLDER = new HashMap<>();
	static {
		CONVERTER_HOLDER.put(SimpleTypeDiffExtractor.class, new SimpleTypeDiffExtractor());
		CONVERTER_HOLDER.put(RichTextTypeExtractor.class, new RichTextTypeExtractor());
		CONVERTER_HOLDER.put(JsonTypeExtractor.class, new JsonTypeExtractor());
		// SPI 加载所有的 转换器类型处理
		ServiceLoader<DiffExtractor> loadedDrivers = ServiceLoader.load(DiffExtractor.class);
		for (DiffExtractor diffExtractor : loadedDrivers) {
			CONVERTER_HOLDER.put(diffExtractor.getClass(), diffExtractor);
		}
	}

	public static DiffExtractor getByClass(Class<? extends DiffExtractor> clazz) {
		DiffExtractor diffExtractor = CONVERTER_HOLDER.get(clazz);
		Assert.notNull(diffExtractor, "diff extractor can not null.");
		return diffExtractor;
	}

	/**
	 * 注册转换器
	 * @author yakir
	 * @date 2021/12/15 15:19
	 * @param clazz
	 * @param diffExtractor
	 */
	public static void register(Class<? extends DiffExtractor> clazz, DiffExtractor diffExtractor) {
		CONVERTER_HOLDER.put(clazz, diffExtractor);
	}

	/**
	 * 默认使用简单类型处理器
	 * @author yakir
	 * @date 2021/12/15 10:06
	 * @return com.relaxed.common.log.action.converter.DiffConverter
	 */
	public static DiffExtractor getByDefault() {
		return CONVERTER_HOLDER.get(SimpleTypeDiffExtractor.class);
	}

}

package com.relaxed.common.log.biz.extractor;

import com.relaxed.common.log.biz.extractor.json.JsonTypeExtractor;
import com.relaxed.common.log.biz.extractor.richtext.RichTextTypeExtractor;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * 差异提取器持有者，用于管理和提供各种类型的差异提取器实例。 该类维护了一个差异提取器的注册表，支持通过 SPI 机制动态加载扩展的提取器。 默认注册了以下提取器： 1.
 * 简单类型提取器（SimpleTypeDiffExtractor） 2. 富文本类型提取器（RichTextTypeExtractor） 3.
 * JSON类型提取器（JsonTypeExtractor） 4. 实体类型提取器（EntityTypeExtractor）
 *
 * @author Yakir
 * @since 1.0.0
 */
public class DiffConvertHolder {

	/**
	 * 差异提取器注册表
	 */
	private static Map<Class<? extends DiffExtractor>, DiffExtractor> CONVERTER_HOLDER = new HashMap<>();

	static {
		// 注册默认的差异提取器
		CONVERTER_HOLDER.put(SimpleTypeDiffExtractor.class, new SimpleTypeDiffExtractor());
		CONVERTER_HOLDER.put(RichTextTypeExtractor.class, new RichTextTypeExtractor());
		CONVERTER_HOLDER.put(JsonTypeExtractor.class, new JsonTypeExtractor());
		CONVERTER_HOLDER.put(EntityTypeExtractor.class, new EntityTypeExtractor());
		// 通过 SPI 机制加载所有扩展的差异提取器
		ServiceLoader<DiffExtractor> loadedDrivers = ServiceLoader.load(DiffExtractor.class);
		for (DiffExtractor diffExtractor : loadedDrivers) {
			CONVERTER_HOLDER.put(diffExtractor.getClass(), diffExtractor);
		}
	}

	/**
	 * 根据类型获取差异提取器实例
	 * @param clazz 差异提取器的类型
	 * @return 差异提取器实例
	 * @throws IllegalArgumentException 如果找不到对应的差异提取器
	 */
	public static DiffExtractor getByClass(Class<? extends DiffExtractor> clazz) {
		DiffExtractor diffExtractor = CONVERTER_HOLDER.get(clazz);
		Assert.notNull(diffExtractor, "diff extractor can not null.");
		return diffExtractor;
	}

	/**
	 * 注册新的差异提取器
	 * @param clazz 差异提取器的类型
	 * @param diffExtractor 差异提取器实例
	 */
	public static void register(Class<? extends DiffExtractor> clazz, DiffExtractor diffExtractor) {
		CONVERTER_HOLDER.put(clazz, diffExtractor);
	}

	/**
	 * 获取默认的差异提取器（简单类型提取器）
	 * @return 简单类型差异提取器实例
	 */
	public static DiffExtractor getByDefault() {
		return CONVERTER_HOLDER.get(SimpleTypeDiffExtractor.class);
	}

}

package com.relaxed.common.risk.engine.cache;

/**
 * @author Yakir
 * @Topic CacheKey
 * @Description
 * @date 2021/8/29 10:29
 * @Version 1.0
 */
public class CacheKey {

	/**
	 * ID 映射model
	 */
	private static final String MODEL_PREFIX = "relaxed:model:";

	/**
	 * model 字段前缀
	 */
	private static final String MODEL_FIELD_PREFIX = "relaxed:model:fields:";

	/**
	 * 预处理字段
	 */
	private static final String MODEL_PRE_ITEMS_PREFIX = "relaxed:model:pre:items:";

	/**
	 * 特征提取
	 */
	private static final String MODEL_ABSTRACTION_PREFIX = "relaxed:model:abstraction:";

	/**
	 * 获取model缓存key
	 * @author yakir
	 * @date 2021/8/29 10:30
	 * @param id 维护 model id ->model 本地缓存
	 * @return java.lang.String
	 */
	public static String getModelCacheKey(Long id) {
		return MODEL_PREFIX + id;
	}

	/**
	 * 获取model对应所有字段缓存
	 * @author yakir
	 * @date 2021/8/29 12:33
	 * @param id 维护model id-> list<FieldVO>
	 * @return java.lang.String
	 */
	public static String getModelFieldCacheKey(Long id) {
		return MODEL_FIELD_PREFIX + id;
	}

	/**
	 * 获取model对应的所有预处理项
	 * @author yakir
	 * @date 2021/8/29 14:10
	 * @param id
	 * @return java.lang.String
	 */
	public static String getModelPreItemCacheKey(Long id) {
		return MODEL_PRE_ITEMS_PREFIX + id;
	}

	/**
	 * 获取model对应的所有特征
	 * @author yakir
	 * @date 2021/8/29 19:21
	 * @param id
	 * @return java.lang.String
	 */
	public static String getModelAbstractionCacheKey(Long id) {
		return MODEL_ABSTRACTION_PREFIX + id;
	}

}

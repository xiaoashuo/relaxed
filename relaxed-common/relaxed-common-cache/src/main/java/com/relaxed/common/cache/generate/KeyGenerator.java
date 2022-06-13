package com.relaxed.common.cache.generate;

/**
 * @author Yakir
 * @Topic CacheKeyGenerator
 * @Description
 * @date 2022/6/13 10:44
 * @Version 1.0
 */
public interface KeyGenerator {

	/**
	 * 构建缓存key
	 * @param originalText 原始文本
	 * @return
	 */
	String generate(String originalText);

}

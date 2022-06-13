package com.relaxed.common.cache;

import com.relaxed.common.cache.generate.KeyGenerator;
import com.relaxed.common.cache.operator.CacheOperator;

/**
 * @author Yakir
 * @Topic CacheActionFunction
 * @Description
 * @date 2022/6/13 12:59
 * @Version 1.0
 */
@FunctionalInterface
public interface CacheAction {

	/**
	 * 缓存命令
	 * @author yakir
	 * @date 2022/6/13 13:01
	 * @param cacheOperator
	 * @param keyGenerate
	 */
	void action(CacheOperator cacheOperator, KeyGenerator keyGenerate);

}

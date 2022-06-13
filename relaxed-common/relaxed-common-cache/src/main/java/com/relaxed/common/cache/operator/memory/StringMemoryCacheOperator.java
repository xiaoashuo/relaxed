package com.relaxed.common.cache.operator.memory;

import cn.hutool.cache.impl.TimedCache;
import com.relaxed.common.cache.operator.memory.AbstractMemoryCacheOperator;

/**
 * @author Yakir
 * @Topic StringMemoryCacheOperator
 * @Description
 * @date 2021/9/1 17:15
 * @Version 1.0
 */
public class StringMemoryCacheOperator extends AbstractMemoryCacheOperator<String> {

	public StringMemoryCacheOperator(TimedCache<String, String> timedCache) {
		super(timedCache);
	}

}

package com.relaxed.common.cache.generate;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

/**
 * @author Yakir
 * @Topic PrefixCacheKeyGenerator
 * @Description
 * @date 2022/6/13 10:46
 * @Version 1.0
 */
@RequiredArgsConstructor
public class PrefixKeyGenerator implements KeyGenerator {

	private final String prefix;

	@Override
	public String generate(String originalText) {
		if (StrUtil.isEmpty(originalText) || StrUtil.isEmpty(prefix)) {
			return originalText;
		}
		return prefix + originalText;
	}

}

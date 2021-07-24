package com.relaxed.common.cache.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author Yakir
 * @Topic CachedInfo
 * @Description
 * @date 2021/7/24 15:08
 * @Version 1.0
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class CachedPutInfo extends MetaAnnotationInfo {

	/**
	 * 过期时间
	 */
	private long ttl;

	public static CachedPutInfo of(String prefix, String key, String suffix, String keyGenerate, String condition,
			Long ttl) {
		CachedPutInfo cachedInfo = new CachedPutInfo();
		cachedInfo.setPrefix(prefix);
		cachedInfo.setKey(key);
		cachedInfo.setSuffix(suffix);
		cachedInfo.setKeyGenerate(keyGenerate);
		cachedInfo.setCondition(condition);
		cachedInfo.setTtl(ttl);
		return cachedInfo;

	}

}

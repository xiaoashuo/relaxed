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
public class CachedDelInfo extends MetaAnnotationInfo {

	public static CachedDelInfo of(String prefix, String key, String suffix, String keyGenerate, String condition) {
		CachedDelInfo cachedInfo = new CachedDelInfo();
		cachedInfo.setPrefix(prefix);
		cachedInfo.setKey(key);
		cachedInfo.setSuffix(suffix);
		cachedInfo.setKeyGenerate(keyGenerate);
		cachedInfo.setCondition(condition);
		return cachedInfo;

	}

}

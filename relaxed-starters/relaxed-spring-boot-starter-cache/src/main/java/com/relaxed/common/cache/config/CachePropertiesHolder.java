package com.relaxed.common.cache.config;

/**
 * @author Yakir
 * @Topic CachePropertiesHolder
 * @Description 缓存配置持有者
 * @date 2021/7/23 17:15
 * @Version 1.0
 */
public class CachePropertiesHolder {
    private static CacheProperties cacheProperties;

    public void setCacheProperties(CacheProperties cacheProperties) {
        CachePropertiesHolder.cacheProperties = cacheProperties;
    }

    public static String keyPrefix() {
        return cacheProperties.getKeyPrefix();
    }

    public static String lockKeySuffix() {
        return cacheProperties.getLockKeySuffix();
    }

    public static String delimiter() {
        return cacheProperties.getDelimiter();
    }

    public static String nullValue() {
        return cacheProperties.getNullValue();
    }

    public static long expireTime() {
        return cacheProperties.getExpireTime();
    }

    public static long lockedTimeOut() {
        return cacheProperties.getLockedTimeOut();
    }
}

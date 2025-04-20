package com.relaxed.common.redis.config;

/**
 * 缓存配置持有者。 提供静态方法访问缓存配置信息，方便在非Spring管理的类中获取配置。
 *
 * @author Hccake
 * @since 1.0
 */
public class CachePropertiesHolder {

	/**
	 * 缓存配置对象
	 */
	private static CacheProperties cacheProperties;

	/**
	 * 设置缓存配置
	 * @param cacheProperties 缓存配置对象
	 */
	public void setCacheProperties(CacheProperties cacheProperties) {
		CachePropertiesHolder.cacheProperties = cacheProperties;
	}

	/**
	 * 获取缓存键前缀
	 * @return 缓存键前缀
	 */
	public static String keyPrefix() {
		return cacheProperties.getKeyPrefix();
	}

	/**
	 * 获取锁键后缀
	 * @return 锁键后缀
	 */
	public static String lockKeySuffix() {
		return cacheProperties.getLockKeySuffix();
	}

	/**
	 * 获取键名分隔符
	 * @return 键名分隔符
	 */
	public static String delimiter() {
		return cacheProperties.getDelimiter();
	}

	/**
	 * 获取空值标识
	 * @return 空值标识
	 */
	public static String nullValue() {
		return cacheProperties.getNullValue();
	}

	/**
	 * 获取默认过期时间
	 * @return 过期时间（秒）
	 */
	public static long expireTime() {
		return cacheProperties.getExpireTime();
	}

	/**
	 * 获取是否启用锁续期
	 * @return 是否启用锁续期
	 */
	public static boolean lockedRenewal() {
		return cacheProperties.isLockRenewal();
	}

	/**
	 * 获取锁续期监听器配置
	 * @return 锁续期监听器配置
	 */
	public static CacheProperties.Watcher watcher() {
		return cacheProperties.getWatcher();
	}

}

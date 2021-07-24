package com.relaxed.common.cache.lock;

import org.springframework.lang.Nullable;

import java.util.concurrent.TimeUnit;

/**
 * @author Yakir
 * @Topic CacheLock
 * @Description
 * @date 2021/7/23 16:44
 * @Version 1.0
 */
public interface CacheManage {

	/**
	 * 根据key 获取值
	 * @param key
	 * @return
	 */
	String get(String key);

	/**
	 * 设置缓存
	 * @param key
	 * @param value
	 */
	void set(String key, String value);

	/**
	 * 设置缓存
	 * @param key
	 * @param val
	 * @param time
	 * @param unit
	 */
	void set(String key, String val, long time, TimeUnit unit);

	/**
	 * 删除缓存 根据key
	 * @param key
	 * @return Boolean
	 */
	Boolean delete(String key);

	/**
	 * 上锁
	 * @param key
	 * @param requestId
	 * @return
	 */
	Boolean lock(String key, String requestId);

	/**
	 * 上锁 指定过期时间
	 * @param key
	 * @param requestId
	 * @param ttl
	 * @return
	 */
	Boolean lock(String key, String requestId, Long ttl);

	/**
	 * 释放锁
	 * @param key
	 * @param requestId
	 * @return
	 */
	Boolean releaseLock(String key, String requestId);

}

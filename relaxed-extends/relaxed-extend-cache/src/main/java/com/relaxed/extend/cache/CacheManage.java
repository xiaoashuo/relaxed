package com.relaxed.extend.cache;

import java.util.Map;
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
    * 获取操作者
    * @author yakir
    * @date 2021/8/27 10:07
    * @return T
    */
	 <T> T getOperator();
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
	 * 上锁
	 * @author yakir
	 * @date 2021/8/26 18:07
	 * @param key
	 * @param requestId
	 * @param ttl 锁过期时间
	 * @param timeout 锁获取超时时间
	 * @return java.lang.Boolean
	 */
	Boolean lock(String key, String requestId, Long ttl, long timeout);

	/**
	 * 释放锁
	 * @param key
	 * @param requestId
	 * @return
	 */
	Boolean releaseLock(String key, String requestId);

}

package com.relaxed.common.cache.lock.operator;

/**
 * @author Yakir
 * @Topic LockCacheOperator
 * @Description
 * @date 2022/6/13 10:56
 * @Version 1.0
 */
public interface LockOperator<T> {

	/**
	 * 上锁
	 * @param key
	 * @param requestId
	 * @return
	 */
	boolean lock(String key, T requestId);

	/**
	 * 上锁 指定过期时间
	 * @param key
	 * @param requestId
	 * @param ttl
	 * @return
	 */
	boolean lock(String key, T requestId, Long ttl);

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
	boolean lock(String key, T requestId, Long ttl, long timeout);

	/**
	 * 释放锁
	 * @param key
	 * @param requestId
	 * @return
	 */
	boolean releaseLock(String key, T requestId);

}

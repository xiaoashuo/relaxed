package com.relaxed.common.cache;

import java.util.concurrent.TimeUnit;

/**
 * @author Yakir
 * @Topic CacheOperator
 * @Description 缓存操作基础接口
 * @date 2021/9/1 16:09
 * @Version 1.0
 */
public interface CacheOperator<T> {

	/**
	 * 添加缓存
	 * @author yakir
	 * @date 2021/9/1 16:09
	 * @param key
	 * @param value
	 */
	void set(String key, T value);

	/**
	 * 添加缓存 默认秒为单位
	 * @author yakir
	 * @date 2021/9/1 16:43
	 * @param key
	 * @param value
	 * @param time
	 */
	default void set(String key, T value, long time) {
		set(key, value, time, TimeUnit.SECONDS);
	}

	/**
	 * 添加缓存
	 * @author yakir
	 * @date 2021/9/1 16:45
	 * @param key
	 * @param value
	 * @param time
	 * @param unit
	 */
	void set(String key, T value, long time, TimeUnit unit);

	/**
	 * 根据key获取值
	 * @author yakir
	 * @date 2021/9/1 18:07
	 * @param key
	 * @return T
	 */
	T get(String key);

	/**
	 * 删除缓存
	 * @author yakir
	 * @date 2021/9/1 16:45
	 * @param key
	 */
	void remove(String key);

	/**
	 * 删除多个缓存
	 * @author yakir
	 * @date 2021/9/1 16:46
	 * @param key
	 */
	void remove(String... key);

	/**
	 * 设置过期时间 默认为 秒
	 * @author yakir
	 * @date 2021/9/1 16:48
	 * @param key
	 * @param time
	 */
	default void expire(String key, long time) {
		expire(key, time, TimeUnit.SECONDS);
	}

	/**
	 * 设置过期时间
	 * @author yakir
	 * @date 2021/9/1 16:48
	 * @param key
	 * @param time
	 * @param unit
	 */
	void expire(String key, long time, TimeUnit unit);

	/**
	 * 是否包含某个缓存
	 * @author yakir
	 * @date 2021/9/1 16:49
	 * @param key
	 * @return boolean
	 */
	boolean contains(String key);

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

	/**
	 * 获得操作者
	 * @author yakir
	 * @date 2021/9/1 16:51
	 * @return O
	 */
	<O> O getOperator();

}

package com.relaxed.common.cache.lock;

/**
 * @author Yakir
 * @Topic CacheLock
 * @Description
 * @date 2021/7/23 16:44
 * @Version 1.0
 */
public interface CacheLock {
    /**
     * 上锁
     * @param key
     * @param requestId
     * @return
     */
    Boolean lock(String key,String requestId);

    /**
     * 上锁 指定过期时间
     * @param key
     * @param requestId
     * @param ttl
     * @return
     */
    Boolean lock(String key,String requestId,Long ttl);

    /**
     * 释放锁
     * @param key
     * @param requestId
     * @return
     */
    Boolean releaseLock(String key,String requestId);
}

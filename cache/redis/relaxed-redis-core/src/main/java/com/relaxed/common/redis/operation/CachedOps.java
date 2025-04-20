package com.relaxed.common.redis.operation;

import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 缓存查询操作类。 封装缓存查询的相关操作，包括分布式锁、缓存读写等功能，继承自AbstractCacheOps。
 *
 * @author Hccake
 * @since 1.0
 */
public class CachedOps extends AbstractCacheOps {

	/**
	 * 返回数据的类型
	 */
	private Type returnType;

	/**
	 * 分布式锁的键名
	 */
	private String lockKey;

	/**
	 * 缓存查询操作，用于从Redis中获取数据
	 */
	private Supplier<String> cacheQuery;

	/**
	 * 缓存更新操作，用于向Redis写入数据
	 */
	private Consumer<Object> cachePut;

	/**
	 * 构造函数
	 * @param joinPoint 切面连接点
	 * @param lockKey 分布式锁的键名
	 * @param cacheQuery 缓存查询操作
	 * @param cachePut 缓存更新操作
	 * @param returnType 返回数据的类型
	 */
	public CachedOps(ProceedingJoinPoint joinPoint, String lockKey, Supplier<String> cacheQuery,
			Consumer<Object> cachePut, Type returnType) {
		super(joinPoint);
		this.lockKey = lockKey;
		this.cacheQuery = cacheQuery;
		this.cachePut = cachePut;
		this.returnType = returnType;
	}

	/**
	 * 获取缓存查询操作
	 * @return 缓存查询操作
	 */
	public Supplier<String> cacheQuery() {
		return cacheQuery;
	}

	/**
	 * 获取缓存更新操作
	 * @return 缓存更新操作
	 */
	public Consumer<Object> cachePut() {
		return cachePut;
	}

	/**
	 * 获取返回数据的类型
	 * @return 返回数据的类型
	 */
	public Type returnType() {
		return returnType;
	}

	/**
	 * 获取分布式锁的键名
	 * @return 分布式锁的键名
	 */
	public String lockKey() {
		return lockKey;
	}

}

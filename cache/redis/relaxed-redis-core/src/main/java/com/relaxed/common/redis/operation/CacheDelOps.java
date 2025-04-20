package com.relaxed.common.redis.operation;

import com.relaxed.common.redis.operation.functions.VoidMethod;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 缓存删除操作类。 封装缓存删除的相关操作，继承自AbstractCacheOps。
 *
 * @author Hccake
 * @since 1.0
 */
public class CacheDelOps extends AbstractCacheOps {

	/**
	 * 删除缓存的具体操作方法
	 */
	private VoidMethod cacheDel;

	/**
	 * 构造函数
	 * @param joinPoint 切面连接点
	 * @param cacheDel 删除缓存的具体操作方法
	 */
	public CacheDelOps(ProceedingJoinPoint joinPoint, VoidMethod cacheDel) {
		super(joinPoint);
		this.cacheDel = cacheDel;
	}

	/**
	 * 获取删除缓存的操作方法
	 * @return 删除缓存的操作方法
	 */
	public VoidMethod cacheDel() {
		return cacheDel;
	}

}

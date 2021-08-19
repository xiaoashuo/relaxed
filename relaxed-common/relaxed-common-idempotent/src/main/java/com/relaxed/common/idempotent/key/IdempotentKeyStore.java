package com.relaxed.common.idempotent.key;

import com.relaxed.common.idempotent.annotation.Idempotent;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @author hccake
 */
public interface IdempotentKeyStore {

	/**
	 * 构建幂等key
	 * @author yakir
	 * @date 2021/8/19 13:57
	 * @param joinPoint
	 * @param idempotentAnnotation
	 * @param method
	 * @param args
	 * @return java.lang.String
	 */
	String buildIdempotentKey(ProceedingJoinPoint joinPoint, Idempotent idempotentAnnotation, Method method,
			Object[] args);

	/**
	 * 当不存在有效 key 时将其存储下来
	 * @param key idempotentKey
	 * @param duration key的有效时长
	 * @return boolean true: 存储成功 false: 存储失败
	 */
	boolean saveIfAbsent(String key, long duration);

	/**
	 * 删除 key
	 * @param key idempotentKey
	 */
	void remove(String key);

}

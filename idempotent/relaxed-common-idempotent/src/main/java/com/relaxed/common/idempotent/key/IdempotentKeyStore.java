package com.relaxed.common.idempotent.key;

import com.relaxed.common.idempotent.annotation.Idempotent;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 幂等键存储接口。 用于管理幂等键的存储和操作，主要功能包括： 1. 构建幂等键 2. 存储幂等键 3. 删除幂等键
 *
 * @author Yakir
 * @since 1.0
 */
public interface IdempotentKeyStore {

	/**
	 * 构建幂等键。 根据连接点、幂等注解和方法参数构建唯一的幂等键。
	 * @param joinPoint 连接点
	 * @param idempotentAnnotation 幂等注解
	 * @return 幂等键
	 */
	String buildIdempotentKey(ProceedingJoinPoint joinPoint, Idempotent idempotentAnnotation);

	/**
	 * 当不存在有效 key 时将其存储下来。 如果 key 已存在，则返回 false；否则存储 key 并返回 true。
	 * @param key 幂等键
	 * @param duration key 的有效时长
	 * @param timeUnit 时长单位
	 * @return true 表示存储成功，false 表示存储失败
	 */
	boolean saveIfAbsent(String key, long duration, TimeUnit timeUnit);

	/**
	 * 删除指定的幂等键。
	 * @param key 幂等键
	 */
	void remove(String key);

}

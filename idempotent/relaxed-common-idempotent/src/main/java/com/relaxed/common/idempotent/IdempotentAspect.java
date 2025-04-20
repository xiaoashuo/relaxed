package com.relaxed.common.idempotent;

import cn.hutool.core.lang.Assert;

import com.relaxed.common.idempotent.annotation.Idempotent;
import com.relaxed.common.idempotent.exception.IdempotentException;
import com.relaxed.common.idempotent.key.IdempotentKeyStore;
import com.relaxed.common.model.result.BaseResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

/**
 * 幂等性切面类。 用于处理带有 {@link Idempotent} 注解的方法的幂等性控制，主要功能包括： 1. 构建幂等键 2. 检查重复请求 3. 执行目标方法 4.
 * 根据配置清理幂等键
 *
 * @author Yakir
 * @since 1.0
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class IdempotentAspect {

	private final IdempotentKeyStore idempotentKeyStore;

	/**
	 * 环绕通知，处理幂等性控制。 1. 构建幂等键 2. 检查是否重复请求 3. 执行目标方法 4. 根据配置清理幂等键
	 * @param joinPoint 连接点
	 * @param idempotentAnnotation 幂等注解
	 * @return 方法执行结果
	 * @throws Throwable 执行过程中的异常
	 */
	@Around("@annotation(idempotentAnnotation)")
	public Object around(ProceedingJoinPoint joinPoint, Idempotent idempotentAnnotation) throws Throwable {
		// 获取幂等标识
		String idempotentKey = idempotentKeyStore.buildIdempotentKey(joinPoint, idempotentAnnotation);

		// 校验当前请求是否重复请求
		boolean saveSuccess = idempotentKeyStore.saveIfAbsent(idempotentKey, idempotentAnnotation.duration(),
				idempotentAnnotation.timeUnit());
		Assert.isTrue(saveSuccess, () -> {
			throw new IdempotentException(BaseResultCode.REPEATED_EXECUTE.getCode(), idempotentAnnotation.message());
		});

		try {
			Object result = joinPoint.proceed();
			if (idempotentAnnotation.removeKeyWhenFinished()) {
				idempotentKeyStore.remove(idempotentKey);
			}
			return result;
		}
		catch (Throwable e) {
			// 异常时，根据配置决定是否删除幂等 key
			if (idempotentAnnotation.removeKeyWhenError()) {
				idempotentKeyStore.remove(idempotentKey);
			}
			throw e;
		}

	}

}

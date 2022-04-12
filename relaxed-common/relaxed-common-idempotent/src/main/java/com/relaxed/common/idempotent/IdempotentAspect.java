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
 * @author hccake
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class IdempotentAspect {

	private final IdempotentKeyStore idempotentKeyStore;

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

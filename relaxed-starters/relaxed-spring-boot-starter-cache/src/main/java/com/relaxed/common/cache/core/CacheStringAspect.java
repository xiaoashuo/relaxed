package com.relaxed.common.cache.core;

import com.relaxed.common.cache.CacheManage;
import com.relaxed.common.cache.lock.DistributedLock;
import com.relaxed.common.cache.lock.LockManage;
import com.relaxed.common.cache.operator.CacheOperator;
import com.relaxed.common.cache.annotation.Cached;
import com.relaxed.common.cache.annotation.CacheDel;
import com.relaxed.common.cache.annotation.CachePut;
import com.relaxed.common.cache.annotation.MetaCacheAnnotation;
import com.relaxed.common.cache.config.CachePropertiesHolder;

import com.relaxed.common.cache.operation.CacheDelOps;
import com.relaxed.common.cache.operation.CachePutOps;
import com.relaxed.common.cache.operation.CachedOps;
import com.relaxed.common.cache.operation.functions.VoidMethod;
import com.relaxed.common.cache.serialize.CacheSerializer;
import com.relaxed.common.core.util.SpELUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Yakir
 * @Topic CacheAspect
 * @Description 为保证缓存更新无异常，该切面优先级必须高于事务切面
 * @date 2021/7/24 12:28
 * @Version 1.0
 */
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@RequiredArgsConstructor
@Slf4j
@Aspect
public class CacheStringAspect {

	private final ApplicationContext applicationContext;

	private final CacheManage<String> cacheManage;

	private final LockManage<String> lockManage;

	private final CacheSerializer cacheSerializer;

	@Pointcut("execution(@(@com.relaxed.common.cache.annotation.MetaCacheAnnotation *) * *(..))")
	public void pointCut() {
	}

	@Around("pointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {

		// 获取目标方法
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();
		log.trace("=======The string cache aop is executed! method : {}", method.getName());
		Object target = point.getTarget();
		Object[] arguments = point.getArgs();
		KeyGenerator keyGenerator = new KeyGenerator(target, method, arguments);
		// 未指定条件 or 条件通过 操作缓存
		MetaCacheAnnotation metaAnnotation = AnnotatedElementUtils.findMergedAnnotation(method,
				MetaCacheAnnotation.class);
		if (!isConditionPassing(metaAnnotation.condition(), keyGenerator)) {
			// 条件未通过 直接执行结果返回
			return point.proceed();
		}
		// 获取注解对象
		Cached cachedAnnotation = AnnotationUtils.getAnnotation(method, Cached.class);
		if (cachedAnnotation != null) {
			// 缓存key
			String key = keyGenerator.getKey(cachedAnnotation.prefix(), cachedAnnotation.keyJoint());
			// redis 分布式锁的 key
			String lockKey = key + CachePropertiesHolder.lockKeySuffix();
			Supplier<String> cacheQuery = () -> cacheManage.get(key);
			// 失效时间控制
			Consumer<Object> cachePut = prodCachePutFunction(cacheManage, key, cachedAnnotation.ttl());
			return cached(new CachedOps(point, lockKey, cacheQuery, cachePut, method.getGenericReturnType()));

		}
		CachePut cachePutAnnotation = AnnotationUtils.getAnnotation(method, CachePut.class);
		if (cachePutAnnotation != null) {
			// 缓存key
			String key = keyGenerator.getKey(cachePutAnnotation.prefix(), cachePutAnnotation.keyJoint());
			// 失效时间控制
			Consumer<Object> cachePut = prodCachePutFunction(cacheManage, key, cachePutAnnotation.ttl());
			return cachePut(new CachePutOps(point, cachePut));

		}
		CacheDel cacheDelAnnotation = AnnotationUtils.getAnnotation(method, CacheDel.class);
		if (cacheDelAnnotation != null) {
			// 缓存key
			String key = keyGenerator.getKey(cacheDelAnnotation.prefix(), cacheDelAnnotation.keyJoint());
			VoidMethod cacheDel = () -> cacheManage.remove(key);
			return cacheDel(new CacheDelOps(point, cacheDel));
		}

		return point.proceed();
	}

	protected boolean isConditionPassing(String condition, KeyGenerator keyGenerator) {
		boolean conditionPassing = true;
		if (StringUtils.hasText(condition)) {
			conditionPassing = keyGenerator.condition(condition);
		}
		return conditionPassing;
	}

	/**
	 * 缓存删除的模板方法 在目标方法执行后 执行删除
	 */
	public Object cacheDel(CacheDelOps ops) throws Throwable {

		// 先执行目标方法 并拿到返回值
		Object data = ops.joinPoint().proceed();
		// 将删除缓存
		ops.cacheDel().run();

		return data;
	}

	/**
	 * 缓存操作模板方法
	 */
	public Object cachePut(CachePutOps ops) throws Throwable {

		// 先执行目标方法 并拿到返回值
		Object data = ops.joinPoint().proceed();

		// 将返回值放置入缓存中
		String cacheData = data == null ? CachePropertiesHolder.nullValue() : cacheSerializer.serialize(data);
		ops.cachePut().accept(cacheData);

		return data;
	}

	/**
	 * cached 类型的模板方法 1. 先查缓存 若有数据则直接返回 2. 尝试获取锁 若成功执行目标方法（一般是去查数据库） 3. 将数据库获取到数据同步至缓存
	 * @param ops 缓存操作类
	 * @return result
	 * @throws Throwable IO 异常
	 */
	public Object cached(CachedOps ops) throws Throwable {

		// 缓存查询方法
		Supplier<String> cacheQuery = ops.cacheQuery();
		// 返回数据类型
		Type dataClazz = ops.returnType();

		// 1.==================尝试从缓存获取数据==========================
		String cacheData = cacheQuery.get();
		// 如果是空值 则return null | 不是空值且不是null 则直接返回
		if (ops.nullValue(cacheData)) {
			return null;
		}
		else if (cacheData != null) {
			return cacheSerializer.deserialize(cacheData, dataClazz);
		}
		// 2.==========如果缓存为空 则需查询数据库并更新===============
		cacheData = DistributedLock.<String>instance().action(ops.lockKey(), () -> {
			String cacheValue = cacheQuery.get();
			if (cacheValue == null) {
				// 从数据库查询数据
				Object dbValue = ops.joinPoint().proceed();
				// 如果数据库中没数据，填充一个String，防止缓存击穿
				cacheValue = dbValue == null ? CachePropertiesHolder.nullValue() : cacheSerializer.serialize(dbValue);
				// 设置缓存
				ops.cachePut().accept(cacheValue);
			}
			return cacheValue;
		}).lockManage(lockManage).onLockFail(cacheQuery).lock();
		// 自旋时间内未获取到锁，或者数据库中数据为空，返回null
		if (cacheData == null || ops.nullValue(cacheData)) {
			return null;
		}
		return cacheSerializer.deserialize(cacheData, dataClazz);
	}

	private Consumer<Object> prodCachePutFunction(CacheManage cacheManage, String key, long ttl) {
		Consumer<Object> cachePut;
		if (ttl < 0) {
			cachePut = value -> cacheManage.set(key, value);
		}
		else if (ttl == 0) {
			cachePut = value -> cacheManage.set(key, value, CachePropertiesHolder.expireTime(), TimeUnit.SECONDS);
		}
		else {
			cachePut = value -> cacheManage.set(key, value, ttl, TimeUnit.SECONDS);
		}
		return cachePut;
	}

	/**
	 * Return a bean with the specified name and type. Used to resolve services that
	 */
	protected <T> T getBean(String beanName, Class<T> expectedType) {
		return applicationContext.getBean(beanName, expectedType);
	}

}

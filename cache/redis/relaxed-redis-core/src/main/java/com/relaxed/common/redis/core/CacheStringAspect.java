package com.relaxed.common.redis.core;

import com.relaxed.common.redis.config.CacheProperties;
import com.relaxed.common.redis.config.CachePropertiesHolder;
import com.relaxed.common.redis.core.annotation.CacheDel;
import com.relaxed.common.redis.core.annotation.CachePut;
import com.relaxed.common.redis.core.annotation.Cached;
import com.relaxed.common.redis.core.annotation.MetaCacheAnnotation;
import com.relaxed.common.redis.lock.DistributedLock;

import com.relaxed.common.redis.lock.scheduled.LockDefinitionHolder;
import com.relaxed.common.redis.lock.scheduled.LockRenewalQueue;
import com.relaxed.common.redis.operation.CacheDelOps;
import com.relaxed.common.redis.operation.CachePutOps;
import com.relaxed.common.redis.operation.CachedOps;
import com.relaxed.common.redis.operation.functions.VoidMethod;
import com.relaxed.common.redis.serialize.RelaxedRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Redis缓存切面。 处理缓存注解的核心切面，实现缓存的查询、更新和删除操作。 为保证缓存更新无异常，该切面优先级必须高于事务切面。
 *
 * @author Yakir
 * @since 1.0
 */
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Slf4j
@Aspect
public class CacheStringAspect {

	/**
	 * Redis字符串操作模板
	 */
	private final StringRedisTemplate redisTemplate;

	/**
	 * Redis序列化器
	 */
	private final RelaxedRedisSerializer relaxedRedisSerializer;

	/**
	 * 构造函数
	 * @param redisTemplate Redis字符串操作模板
	 * @param relaxedRedisSerializer Redis序列化器
	 */
	public CacheStringAspect(StringRedisTemplate redisTemplate, RelaxedRedisSerializer relaxedRedisSerializer) {
		this.redisTemplate = redisTemplate;
		this.relaxedRedisSerializer = relaxedRedisSerializer;
	}

	/**
	 * 定义切点，匹配所有带有MetaCacheAnnotation注解的方法
	 */
	@Pointcut("execution(@(@com.relaxed.common.redis.core.annotation.MetaCacheAnnotation *) * *(..))")
	public void pointCut() {
	}

	/**
	 * 环绕通知，处理缓存注解
	 * @param point 连接点
	 * @return 方法执行结果
	 * @throws Throwable 执行过程中的异常
	 */
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
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		// 获取注解对象
		Cached cachedAnnotation = AnnotationUtils.getAnnotation(method, Cached.class);
		if (cachedAnnotation != null) {
			// 缓存key
			String key = keyGenerator.getKey(cachedAnnotation.prefix(), cachedAnnotation.keyJoint());
			// redis 分布式锁的 key
			String lockKey = key + CachePropertiesHolder.lockKeySuffix();
			Supplier<String> cacheQuery = () -> valueOperations.get(key);
			// 失效时间控制
			Consumer<Object> cachePut = prodCachePutFunction(valueOperations, key, cachedAnnotation.ttl(),
					cachedAnnotation.timeUnit());
			return cached(new CachedOps(point, lockKey, cacheQuery, cachePut, method.getGenericReturnType()));
		}

		CachePut cachePutAnnotation = AnnotationUtils.getAnnotation(method, CachePut.class);
		if (cachePutAnnotation != null) {
			// 缓存key
			String key = keyGenerator.getKey(cachePutAnnotation.prefix(), cachePutAnnotation.keyJoint());
			// 失效时间控制
			Consumer<Object> cachePut = prodCachePutFunction(valueOperations, key, cachePutAnnotation.ttl(),
					cachePutAnnotation.timeUnit());
			return cachePut(new CachePutOps(point, cachePut));
		}

		CacheDel cacheDelAnnotation = AnnotationUtils.getAnnotation(method, CacheDel.class);
		if (cacheDelAnnotation != null) {
			// 缓存key
			VoidMethod cacheDel;
			if (cacheDelAnnotation.multiDel()) {
				Collection<String> keys = keyGenerator.getKeys(cacheDelAnnotation.prefix(),
						cacheDelAnnotation.keyJoint());
				cacheDel = () -> redisTemplate.delete(keys);
			}
			else {
				// 缓存key
				String key = keyGenerator.getKey(cacheDelAnnotation.prefix(), cacheDelAnnotation.keyJoint());
				cacheDel = () -> redisTemplate.delete(key);
			}
			return cacheDel(new CacheDelOps(point, cacheDel));
		}

		return point.proceed();
	}

	/**
	 * 判断缓存条件是否通过
	 * @param condition 条件表达式
	 * @param keyGenerator 键生成器
	 * @return 条件是否通过
	 */
	protected boolean isConditionPassing(String condition, KeyGenerator keyGenerator) {
		boolean conditionPassing = true;
		if (StringUtils.hasText(condition)) {
			conditionPassing = keyGenerator.condition(condition);
		}
		return conditionPassing;
	}

	/**
	 * 处理缓存删除操作 在目标方法执行后执行删除
	 * @param ops 缓存删除操作
	 * @return 方法执行结果
	 * @throws Throwable 执行过程中的异常
	 */
	public Object cacheDel(CacheDelOps ops) throws Throwable {
		// 先执行目标方法 并拿到返回值
		Object data = ops.joinPoint().proceed();
		// 将删除缓存
		ops.cacheDel().run();
		return data;
	}

	/**
	 * 处理缓存更新操作
	 * @param ops 缓存更新操作
	 * @return 方法执行结果
	 * @throws Throwable 执行过程中的异常
	 */
	public Object cachePut(CachePutOps ops) throws Throwable {
		// 先执行目标方法 并拿到返回值
		Object data = ops.joinPoint().proceed();
		// 将返回值放置入缓存中
		String cacheData = data == null ? CachePropertiesHolder.nullValue() : relaxedRedisSerializer.serialize(data);
		ops.cachePut().accept(cacheData);
		return data;
	}

	/**
	 * 处理缓存查询操作 1. 先查缓存，若有数据则直接返回 2. 尝试获取锁，若成功执行目标方法（一般是去查数据库） 3. 将数据库获取到数据同步至缓存
	 * @param ops 缓存查询操作
	 * @return 方法执行结果
	 * @throws Throwable 执行过程中的异常
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
			return relaxedRedisSerializer.deserialize(cacheData, dataClazz);
		}
		// 2.==========如果缓存为空 则需查询数据库并更新===============
		// 缓存时间单位 锁也延用此时间单位
		CacheProperties.Watcher watcher = CachePropertiesHolder.watcher();
		long lockedTimeOut = watcher.getLockedTimeOut();
		TimeUnit lockTimeUnit = watcher.getLockTimeUnit();
		cacheData = DistributedLock.<String>instance().action(ops.lockKey(), () -> {
			String cacheValue = cacheQuery.get();
			if (cacheValue == null) {
				if (CachePropertiesHolder.lockedRenewal()) {
					// 开启线程监控 锁续期
					Thread currentThread = Thread.currentThread();
					LockDefinitionHolder lockDefinitionHolder = new LockDefinitionHolder(ops.lockKey(), lockedTimeOut,
							System.currentTimeMillis(), currentThread, watcher.getLockRenewalCount(), lockTimeUnit);
					LockRenewalQueue.holderList.add(lockDefinitionHolder);
				}
				// 从数据库查询数据
				Object dbValue = ops.joinPoint().proceed();
				// 如果数据库中没数据，填充一个String，防止缓存击穿
				cacheValue = dbValue == null ? CachePropertiesHolder.nullValue()
						: relaxedRedisSerializer.serialize(dbValue);
				// 设置缓存
				ops.cachePut().accept(cacheValue);
			}
			return cacheValue;
		}).lockTimeOut(lockedTimeOut).lockTimeUnit(lockTimeUnit).onLockFail(cacheQuery).lock();
		// 自旋时间内未获取到锁，或者数据库中数据为空，返回null
		if (cacheData == null || ops.nullValue(cacheData)) {
			return null;
		}
		return relaxedRedisSerializer.deserialize(cacheData, dataClazz);
	}

	/**
	 * 生成缓存更新函数
	 * @param valueOperations Redis值操作对象
	 * @param key 缓存键
	 * @param ttl 过期时间
	 * @param unit 时间单位
	 * @return 缓存更新函数
	 */
	private Consumer<Object> prodCachePutFunction(ValueOperations<String, String> valueOperations, String key, long ttl,
			TimeUnit unit) {
		Consumer<Object> cachePut;
		if (ttl < 0) {
			cachePut = value -> valueOperations.set(key, (String) value);
		}
		else if (ttl == 0) {
			cachePut = value -> valueOperations.set(key, (String) value, CachePropertiesHolder.expireTime(), unit);
		}
		else {
			cachePut = value -> valueOperations.set(key, (String) value, ttl, unit);
		}
		return cachePut;
	}

}

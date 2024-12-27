package com.relaxed.test.cache.redis.controller;

import com.relaxed.test.cache.redis.service.TestCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yakir
 * @Topic CacheController
 * @Description
 * @date 2021/7/24 14:15
 * @Version 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/cache")
public class TestCacheController {

	private final TestCacheService testCacheService;

	/**
	 * 根据条件决定是否缓存
	 * @param key
	 * @param value
	 * @param timeout
	 * @return
	 */
	@GetMapping("/set")
	public String cacheSet(String key, String value, Long timeout) {
		return testCacheService.cacheSet(key, value, timeout);
	}

	@GetMapping("/get")
	public String cacheSet(String key) {
		return testCacheService.cacheGet(key);
	}

	@GetMapping("/delete")
	public String cacheDelete(String key) {
		return testCacheService.cacheDelete(key);
	}

	/**
	 * 根据条件决定是否缓存
	 * @param param
	 * @return
	 */
	@GetMapping("/lock/condition")
	public String cacheLockCondition(Integer param) {
		return testCacheService.cacheLockCondition(param);
	}

	/**
	 * 带前缀缓存
	 * @param param
	 * @return
	 */
	@GetMapping("/lock/prefix")
	public String cacheLockPrefix(Integer param) {
		return testCacheService.cacheLockPrefix(param);
	}

	@GetMapping("/lock/suffix")
	public String cacheLockSuffix(Integer param) {
		return testCacheService.cacheLockSuffix(param);
	}

	@GetMapping("/lock/all")
	public String cacheLockPrefixSuffix(Integer param) {
		return testCacheService.cacheLockPrefixSuffix(param);
	}

	@GetMapping("/lock/gen")
	public String cacheLockKeyGenerator(Integer param) {
		return testCacheService.cacheLockKeyGenerator(param);
	}

	@GetMapping("/lock/time")
	public String cacheLockKeyTime(Integer param) {
		return testCacheService.cacheLockKeyTime(param);
	}

	@GetMapping("/lock/del")
	public String cacheDel(Integer param) {
		return testCacheService.cacheDel(param);
	}

	@GetMapping("/lock/put")
	public String cachePut(Integer param) {
		return testCacheService.cachePut(param);
	}

	@GetMapping("/lock/no")
	public String cacheNo(Integer param) {
		return testCacheService.noCache(param);
	}

	@GetMapping("/lock/renewal")
	public String lockRenewal(Integer param) {
		String s = testCacheService.lockRenewal(param);
		return s;
	}

}

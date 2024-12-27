package com.relaxed.test.cache.redis;

import com.relaxed.common.cache.RelaxedRedisAutoConfiguration;
import com.relaxed.common.redis.RedisHelper;
import com.relaxed.common.redis.lock.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Yakir
 * @Topic RedisTest
 * @Description
 * @date 2024/12/27 16:11
 * @Version 1.0
 */
@Slf4j
@SpringBootTest(
		classes = { RelaxedRedisAutoConfiguration.class, JacksonAutoConfiguration.class, RedisAutoConfiguration.class },
		properties = "spring.config.location=classpath:/redis/application-redis.yml")
public class RedisTest {

	String lockKey = "relaxed";

	@Test
	public void testSetCache() {
		RedisHelper.set("test", "123");
		String val = RedisHelper.get("test");
		log.info("当前缓存值:{}", val);
	}

	@Test
	public void testLock() {
		String value = DistributedLock.<String>instance().action(this.lockKey, () -> "value")
				.onSuccess(ret -> ret + "success").lock();
		log.info("获取到结果值:{}", value);

	}

}

package com.relaxed.test.cache.redis.subscribe;

import com.relaxed.common.redis.distributor.EventDistributor;
import com.relaxed.common.redis.distributor.subscribe.SubscribeHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SubscribeConfiguration
 *
 * @author Yakir
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class SubscribeConfiguration {

	private final EventDistributor eventDistributor;

	/**
	 * 订阅者注册器
	 * @return SubscribeRegister
	 */
	@Bean
	public SubscribeRegister subscribeRegister() {
		return new SubscribeRegister(eventDistributor);
	}

}

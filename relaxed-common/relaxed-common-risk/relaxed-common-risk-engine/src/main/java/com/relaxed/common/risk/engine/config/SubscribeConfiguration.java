package com.relaxed.common.risk.engine.config;

import com.relaxed.common.risk.engine.cache.CacheService;
import com.relaxed.common.risk.engine.core.distributor.EventDistributor;
import com.relaxed.common.risk.engine.core.distributor.subscribe.SubscribeHandle;
import com.relaxed.common.risk.engine.core.distributor.subscribe.SubscribeHolder;
import com.relaxed.common.risk.engine.core.distributor.subscribe.SubscribeRegister;
import com.relaxed.common.risk.engine.core.distributor.subscribe.handles.SubscribeModelHandle;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yakir
 * @Topic SubscribeConfiguration
 * @Description
 * @date 2021/8/29 10:06
 * @Version 1.0
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class SubscribeConfiguration {

	private final CacheService cacheService;

	private final EventDistributor eventDistributor;

	/**
	 * 订阅者注册器
	 * @author yakir
	 * @date 2021/8/29 10:09
	 * @return com.relaxed.common.risk.engine.core.distributor.subscribe.SubscribeRegister
	 */
	@Bean
	public SubscribeRegister subscribeRegister() {
		return new SubscribeRegister(eventDistributor);
	}

	/**
	 * 订阅holder 容器
	 * @author yakir
	 * @date 2021/8/29 10:08
	 * @return com.relaxed.common.risk.engine.core.distributor.subscribe.SubscribeHolder
	 */
	@Bean
	public SubscribeHolder subscribeHolder() {
		return new SubscribeHolder();
	}

	/**
	 * 订阅model处理器
	 * @author yakir
	 * @date 2021/8/29 10:07
	 * @return com.relaxed.common.risk.engine.core.distributor.subscribe.SubscribeHandle
	 */
	@Bean
	public SubscribeHandle subscribeModelHandle() {
		return new SubscribeModelHandle(cacheService);
	}

}

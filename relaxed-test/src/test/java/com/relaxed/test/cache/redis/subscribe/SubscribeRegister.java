package com.relaxed.test.cache.redis.subscribe;

import com.relaxed.common.redis.distributor.EventDistributor;
import com.relaxed.common.redis.distributor.subscribe.SubscribeHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

/**
 * SubscribeRegister
 *
 * @author Yakir
 */
@Slf4j
@RequiredArgsConstructor
public class SubscribeRegister implements InitializingBean {

	private final EventDistributor eventDistributor;

	@Override
	public void afterPropertiesSet() throws Exception {
		registerSubscribe();
	}

	private void registerSubscribe() {
		// 注册所有订阅者
		for (SubscribeEnum value : SubscribeEnum.values()) {
			String channel = value.getChannel();
			eventDistributor.subscribe(channel, SubscribeHolder.getByChannel(channel));
		}
	}

}

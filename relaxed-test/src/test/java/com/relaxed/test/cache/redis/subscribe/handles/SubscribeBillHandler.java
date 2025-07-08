package com.relaxed.test.cache.redis.subscribe.handles;

import com.relaxed.common.redis.distributor.subscribe.SubscribeHandle;
import com.relaxed.common.redis.distributor.subscribe.SubscribeType;
import com.relaxed.test.cache.redis.subscribe.SubscribeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * SubscribeBillHandler
 *
 * @author Yakir
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SubscribeBillHandler implements SubscribeHandle {

	@Override
	public SubscribeType type() {
		return SubscribeEnum.PUB_SUB_TRADE_FILL;
	}

	@Override
	public void onMessage(String channel, String message) {
		log.info("bill fill update message:{}", message);
	}

}
